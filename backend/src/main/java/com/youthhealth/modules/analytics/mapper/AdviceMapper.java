package com.youthhealth.modules.analytics.mapper;

import com.youthhealth.modules.analytics.entity.AdviceLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdviceMapper {
    int insert(AdviceLog adviceLog);

    AdviceLog findLatestByUserId(@Param("userId") Long userId);
}
