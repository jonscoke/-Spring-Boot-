package com.youthhealth.modules.analytics.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SportTrendPointVO {
    private LocalDate date;
    private BigDecimal caloriesBurned;
    private Integer steps;
    private Integer durationMin;
}
