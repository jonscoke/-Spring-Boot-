package com.youthhealth.modules.analytics.mapper;

import com.youthhealth.modules.analytics.vo.DietTrendPointVO;
import com.youthhealth.modules.analytics.vo.SportTrendPointVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface StatsMapper {
    BigDecimal sumDietCaloriesByDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);

    BigDecimal sumSportCaloriesByDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);

    List<SportTrendPointVO> sportTrendByDateRange(@Param("userId") Long userId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    List<DietTrendPointVO> dietTrendByDateRange(@Param("userId") Long userId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    BigDecimal latestWeightKg(@Param("userId") Long userId);

    LocalDateTime latestWeightUpdatedAt(@Param("userId") Long userId);
}
