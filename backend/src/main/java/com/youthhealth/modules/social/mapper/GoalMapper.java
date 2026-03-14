package com.youthhealth.modules.social.mapper;

import com.youthhealth.modules.social.entity.UserGoal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoalMapper {
    int insert(UserGoal goal);

    int updateByIdAndUserId(UserGoal goal);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    UserGoal findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Long countByUserId(@Param("userId") Long userId);

    List<UserGoal> pageByUserId(@Param("userId") Long userId,
                                @Param("offset") Integer offset,
                                @Param("size") Integer size);
}
