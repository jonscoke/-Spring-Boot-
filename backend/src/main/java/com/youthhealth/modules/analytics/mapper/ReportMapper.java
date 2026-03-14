package com.youthhealth.modules.analytics.mapper;

import com.youthhealth.modules.analytics.entity.MonthlyReport;
import com.youthhealth.modules.analytics.entity.WeeklyReport;
import com.youthhealth.modules.analytics.vo.ReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    int insertWeekly(WeeklyReport weeklyReport);

    int insertMonthly(MonthlyReport monthlyReport);

    Long countPage(@Param("userId") Long userId, @Param("reportType") String reportType);

    List<ReportVO> page(@Param("userId") Long userId,
                        @Param("reportType") String reportType,
                        @Param("offset") Integer offset,
                        @Param("size") Integer size);
}
