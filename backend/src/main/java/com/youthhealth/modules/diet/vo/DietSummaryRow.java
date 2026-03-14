package com.youthhealth.modules.diet.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DietSummaryRow {
    private Long recordCount;
    private BigDecimal totalCalories;
    private BigDecimal totalProtein;
    private BigDecimal totalFat;
    private BigDecimal totalCarb;
}
