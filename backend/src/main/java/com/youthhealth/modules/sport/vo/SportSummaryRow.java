package com.youthhealth.modules.sport.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SportSummaryRow {
    private Long recordCount;
    private Integer totalSteps;
    private BigDecimal totalDistanceKm;
    private Integer totalDurationMin;
    private BigDecimal totalCaloriesBurned;
}
