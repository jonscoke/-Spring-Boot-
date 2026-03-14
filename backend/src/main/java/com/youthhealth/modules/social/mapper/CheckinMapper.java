package com.youthhealth.modules.social.mapper;

import com.youthhealth.modules.social.entity.UserCheckin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CheckinMapper {
    int insert(UserCheckin checkin);

    UserCheckin findByUserIdAndDate(@Param("userId") Long userId, @Param("checkinDate") LocalDate checkinDate);

    Long countByUserId(@Param("userId") Long userId);

    List<UserCheckin> pageByUserId(@Param("userId") Long userId,
                                   @Param("offset") Integer offset,
                                   @Param("size") Integer size);
}
