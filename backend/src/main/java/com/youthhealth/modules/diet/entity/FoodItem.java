package com.youthhealth.modules.diet.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FoodItem {
    private Long id;
    private String foodName;
    private String foodCategory;
    private String unit;
    private BigDecimal caloriePer100g;
    private BigDecimal proteinPer100g;
    private BigDecimal fatPer100g;
    private BigDecimal carbPer100g;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
