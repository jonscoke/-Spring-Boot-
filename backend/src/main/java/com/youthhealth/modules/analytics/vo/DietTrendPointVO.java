package com.youthhealth.modules.analytics.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DietTrendPointVO {
    private LocalDate date;
    private BigDecimal caloriesIntake;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carb;
}
