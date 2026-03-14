package com.youthhealth.modules.diet.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DietRecord {
    private Long id;
    private Long userId;
    private String mealType;
    private LocalDate recordDate;
    private BigDecimal totalCalories;
    private BigDecimal totalProtein;
    private BigDecimal totalFat;
    private BigDecimal totalCarb;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
