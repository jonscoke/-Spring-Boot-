package com.youthhealth.modules.social.service;

import com.youthhealth.modules.social.dto.CommentCreateRequest;
import com.youthhealth.modules.social.dto.PostCreateRequest;
import com.youthhealth.modules.social.entity.SocialPost;
import com.youthhealth.modules.social.vo.CommentVO;
import com.youthhealth.modules.social.vo.PageVO;
import com.youthhealth.modules.social.vo.PostPageItemVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SocialService {
    SocialPost createPost(Long userId, PostCreateRequest request);

    PageVO<PostPageItemVO> pagePosts(Long currentUserId, Integer page, Integer size);

    List<CommentVO> listComments(Long postId);

    CommentVO createComment(Long userId, Long postId, CommentCreateRequest request);

    void like(Long userId, Long postId);

    void unlike(Long userId, Long postId);

    void adminDeletePost(Long postId);

    String uploadPostImage(MultipartFile file);
}
