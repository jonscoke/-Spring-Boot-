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
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final SocialMapper socialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SocialPost createPost(Long userId, PostCreateRequest request) {
        SocialPost post = new SocialPost();
        post.setUserId(userId);
        post.setContent(request.getContent().trim());
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
        return PageVO.<PostPageItemVO>builder()
                .total(total == null ? 0L : total)
                .page(safePage)
                .size(safeSize)
                .records(records)
                .build();
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

    private SocialPost requirePost(Long postId) {
        SocialPost post = socialMapper.findPostById(postId);
        if (post == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return post;
    }
}
