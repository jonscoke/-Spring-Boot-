package com.youthhealth.modules.sport.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SportRecord {
    private Long id;
    private Long userId;
    private String sportType;
    private Integer steps;
    private BigDecimal distanceKm;
    private Integer durationMin;
    private BigDecimal pace;
    private BigDecimal caloriesBurned;
    private String sourceType;
    private String externalId;
    private LocalDate recordDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
