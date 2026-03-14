package com.youthhealth.modules.sport.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SportSummaryVO {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long recordCount;
    private Integer totalSteps;
    private BigDecimal totalDistanceKm;
    private Integer totalDurationMin;
    private BigDecimal totalCaloriesBurned;
}
