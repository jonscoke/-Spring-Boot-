package com.youthhealth.modules.diet.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class DietSummaryVO {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long recordCount;
    private BigDecimal totalCalories;
    private BigDecimal totalProtein;
    private BigDecimal totalFat;
    private BigDecimal totalCarb;
}
