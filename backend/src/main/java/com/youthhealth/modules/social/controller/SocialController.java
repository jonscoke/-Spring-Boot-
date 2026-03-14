package com.youthhealth.modules.social.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.social.dto.CommentCreateRequest;
import com.youthhealth.modules.social.dto.PostCreateRequest;
import com.youthhealth.modules.social.entity.SocialPost;
import com.youthhealth.modules.social.service.SocialService;
import com.youthhealth.modules.social.vo.CommentVO;
import com.youthhealth.modules.social.vo.PageVO;
import com.youthhealth.modules.social.vo.PostPageItemVO;
import com.youthhealth.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;

    @PostMapping("/api/posts")
    public Result<SocialPost> createPost(@Valid @RequestBody PostCreateRequest request) {
        return Result.success(socialService.createPost(currentUserId(), request));
    }

    @GetMapping("/api/posts/page")
    public Result<PageVO<PostPageItemVO>> pagePosts(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(socialService.pagePosts(currentUserId(), page, size));
    }

    @GetMapping("/api/posts/{id}/comments")
    public Result<List<CommentVO>> listComments(@PathVariable Long id) {
        return Result.success(socialService.listComments(id));
    }

    @PostMapping("/api/posts/{id}/comments")
    public Result<CommentVO> createComment(@PathVariable Long id, @Valid @RequestBody CommentCreateRequest request) {
        return Result.success(socialService.createComment(currentUserId(), id, request));
    }

    @PostMapping(value = "/api/posts/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Map<String, String>> uploadPostImage(@RequestPart("file") MultipartFile file) {
        return Result.success(Map.of("url", socialService.uploadPostImage(file)));
    }

    @PostMapping("/api/posts/{id}/likes")
    public Result<Void> like(@PathVariable Long id) {
        socialService.like(currentUserId(), id);
        return Result.success();
    }

    @DeleteMapping("/api/posts/{id}/likes")
    public Result<Void> unlike(@PathVariable Long id) {
        socialService.unlike(currentUserId(), id);
        return Result.success();
    }

    @DeleteMapping("/api/admin/posts/{id}")
    public Result<Void> adminDeletePost(@PathVariable Long id) {
        if (!SecurityUtils.hasRole("ADMIN")) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        socialService.adminDeletePost(id);
        return Result.success();
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.currentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
}
