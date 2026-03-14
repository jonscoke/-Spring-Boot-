package com.youthhealth.modules.social.service.impl;

import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.social.dto.CommentCreateRequest;
import com.youthhealth.modules.social.dto.PostCreateRequest;
import com.youthhealth.modules.social.entity.SocialComment;
import com.youthhealth.modules.social.entity.SocialLike;
import com.youthhealth.modules.social.entity.SocialPost;
import com.youthhealth.modules.social.mapper.SocialMapper;
import com.youthhealth.modules.social.service.SocialService;
import com.youthhealth.modules.social.vo.CommentVO;
import com.youthhealth.modules.social.vo.PageVO;
import com.youthhealth.modules.social.vo.PostPageItemVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final SocialMapper socialMapper;
    private final ObjectMapper objectMapper;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SocialPost createPost(Long userId, PostCreateRequest request) {
        SocialPost post = new SocialPost();
        post.setUserId(userId);
        post.setContent(serializePostContent(request.getContent(), request.getImages()));
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus("NORMAL");
        socialMapper.insertPost(post);
        return post;
    }

    @Override
    public PageVO<PostPageItemVO> pagePosts(Long currentUserId, Integer page, Integer size) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        int offset = (safePage - 1) * safeSize;
        Long total = socialMapper.countPosts();
        List<PostPageItemVO> records = total == null || total == 0 ? List.of() : socialMapper.pagePosts(currentUserId, offset, safeSize);
        records.forEach(this::fillPostContent);
        return PageVO.<PostPageItemVO>builder()
                .total(total == null ? 0L : total)
                .page(safePage)
                .size(safeSize)
                .records(records)
                .build();
    }

    @Override
    public List<CommentVO> listComments(Long postId) {
        requirePost(postId);
        return socialMapper.listCommentsByPostId(postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO createComment(Long userId, Long postId, CommentCreateRequest request) {
        SocialPost post = requirePost(postId);
        SocialComment comment = new SocialComment();
        comment.setPostId(post.getId());
        comment.setUserId(userId);
        comment.setContent(request.getContent().trim());
        socialMapper.insertComment(comment);
        socialMapper.increaseCommentCount(postId);
        return CommentVO.builder()
                .id(comment.getId())
                .postId(postId)
                .userId(userId)
                .authorNickname(socialMapper.findNicknameByUserId(userId))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void like(Long userId, Long postId) {
        requirePost(postId);
        if (socialMapper.findLike(postId, userId) != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "already liked");
        }
        SocialLike socialLike = new SocialLike();
        socialLike.setPostId(postId);
        socialLike.setUserId(userId);
        try {
            socialMapper.insertLike(socialLike);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "already liked");
        }
        socialMapper.increaseLikeCount(postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlike(Long userId, Long postId) {
        requirePost(postId);
        if (socialMapper.deleteLike(postId, userId) > 0) {
            socialMapper.decreaseLikeCount(postId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminDeletePost(Long postId) {
        if (socialMapper.markPostDeleted(postId) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }

    @Override
    public String uploadPostImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "image file is required");
        }
        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !contentType.startsWith("image/")) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "only image upload is supported");
        }

        String extension = resolveExtension(file.getOriginalFilename(), contentType);
        Path targetDirectory = Paths.get(uploadDir, "community").toAbsolutePath().normalize();
        String filename = UUID.randomUUID() + extension;
        Path targetFile = targetDirectory.resolve(filename);

        try {
            Files.createDirectories(targetDirectory);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), "failed to store image");
        }

        return "/uploads/community/" + filename;
    }

    private SocialPost requirePost(Long postId) {
        SocialPost post = socialMapper.findPostById(postId);
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return post;
    }

    private void fillPostContent(PostPageItemVO record) {
        Map<String, Object> payload = parsePostPayload(record.getContent());
        if (payload == null) {
            record.setImages(List.of());
            return;
        }
        Object text = payload.get("text");
        Object images = payload.get("images");
        record.setContent(text instanceof String ? (String) text : "");
        if (images instanceof List<?> imageList) {
            record.setImages(imageList.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList());
            return;
        }
        record.setImages(List.of());
    }

    private String serializePostContent(String content, List<String> images) {
        List<String> safeImages = images == null ? List.of() : images.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .toList();
        try {
            return objectMapper.writeValueAsString(Map.of(
                    "text", content.trim(),
                    "images", safeImages
            ));
        } catch (JsonProcessingException ex) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), "failed to save post content");
        }
    }

    private Map<String, Object> parsePostPayload(String rawContent) {
        if (!StringUtils.hasText(rawContent)) {
            return Map.of("text", "", "images", List.of());
        }
        String content = rawContent.trim();
        if (!content.startsWith("{")) {
            return null;
        }
        try {
            return objectMapper.readValue(content, new TypeReference<>() {
            });
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    private String resolveExtension(String originalFilename, String contentType) {
        String ext = StringUtils.getFilenameExtension(originalFilename);
        if (StringUtils.hasText(ext)) {
            return "." + ext.toLowerCase();
        }
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".img";
        };
    }
}
