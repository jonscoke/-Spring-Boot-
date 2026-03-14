package com.youthhealth.modules.auth.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class UserProfileVO {
    private String gender;
    private LocalDate birthday;
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private String activityLevel;
    private String goalType;
    private BigDecimal bmi;
    private BigDecimal bmr;
    private BigDecimal dailyCalorieTarget;
}
