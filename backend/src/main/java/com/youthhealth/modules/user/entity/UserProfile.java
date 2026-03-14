package com.youthhealth.modules.user.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserProfile {
    private Long id;
    private Long userId;
    private String gender;
    private LocalDate birthday;
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private String activityLevel;
    private String goalType;
    private BigDecimal bmi;
    private BigDecimal bmr;
    private BigDecimal dailyCalorieTarget;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
