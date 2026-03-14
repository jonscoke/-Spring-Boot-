package com.youthhealth.modules.diet.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class NutritionRatioVO {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal proteinCalorieRatio;
    private BigDecimal fatCalorieRatio;
    private BigDecimal carbCalorieRatio;
}
