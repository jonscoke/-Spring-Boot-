package com.youthhealth.modules.social.mapper;

import com.youthhealth.modules.social.entity.SocialComment;
import com.youthhealth.modules.social.entity.SocialLike;
import com.youthhealth.modules.social.entity.SocialPost;
import com.youthhealth.modules.social.vo.PostPageItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SocialMapper {
    int insertPost(SocialPost post);

    SocialPost findPostById(@Param("id") Long id);

    int markPostDeleted(@Param("id") Long id);

    Long countPosts();

    List<PostPageItemVO> pagePosts(@Param("currentUserId") Long currentUserId,
                                   @Param("offset") Integer offset,
                                   @Param("size") Integer size);

    int insertComment(SocialComment comment);

    int increaseCommentCount(@Param("postId") Long postId);

    int insertLike(SocialLike socialLike);

    SocialLike findLike(@Param("postId") Long postId, @Param("userId") Long userId);

    int deleteLike(@Param("postId") Long postId, @Param("userId") Long userId);

    int increaseLikeCount(@Param("postId") Long postId);

    int decreaseLikeCount(@Param("postId") Long postId);

    String findNicknameByUserId(@Param("userId") Long userId);
}
