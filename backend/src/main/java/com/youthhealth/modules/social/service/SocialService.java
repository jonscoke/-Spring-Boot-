package com.youthhealth.modules.social.service;

import com.youthhealth.modules.social.dto.CommentCreateRequest;
import com.youthhealth.modules.social.dto.PostCreateRequest;
import com.youthhealth.modules.social.entity.SocialPost;
import com.youthhealth.modules.social.vo.CommentVO;
import com.youthhealth.modules.social.vo.PageVO;
import com.youthhealth.modules.social.vo.PostPageItemVO;

public interface SocialService {
    SocialPost createPost(Long userId, PostCreateRequest request);

    PageVO<PostPageItemVO> pagePosts(Long currentUserId, Integer page, Integer size);

    CommentVO createComment(Long userId, Long postId, CommentCreateRequest request);

    void like(Long userId, Long postId);

    void unlike(Long userId, Long postId);

    void adminDeletePost(Long postId);
}
