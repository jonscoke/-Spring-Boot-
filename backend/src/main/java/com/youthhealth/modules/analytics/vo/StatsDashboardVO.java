package com.youthhealth.modules.analytics.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class StatsDashboardVO {
    private BigDecimal todayIntakeCalories;
    private BigDecimal todayBurnCalories;
    private BigDecimal todayEnergyGap;
    private List<SportTrendPointVO> sportTrend7d;
    private List<DietTrendPointVO> dietTrend7d;
    private List<WeightTrendPointVO> weightTrend;
}
