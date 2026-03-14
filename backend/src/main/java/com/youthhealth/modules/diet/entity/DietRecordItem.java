package com.youthhealth.modules.diet.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DietRecordItem {
    private Long id;
    private Long dietRecordId;
    private Long foodItemId;
    private String foodName;
    private BigDecimal intakeGram;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carb;
    private LocalDateTime createdAt;
}
