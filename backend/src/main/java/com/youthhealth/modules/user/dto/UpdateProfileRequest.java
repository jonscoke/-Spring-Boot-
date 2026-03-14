package com.youthhealth.modules.user.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    @NotNull
    @Pattern(regexp = "^(MALE|FEMALE)$")
    private String gender;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;

    @NotNull
    @DecimalMin(value = "100.0")
    @DecimalMax(value = "250.0")
    private BigDecimal heightCm;

    @NotNull
    @DecimalMin(value = "25.0")
    @DecimalMax(value = "250.0")
    private BigDecimal weightKg;

    @NotNull
    @Pattern(regexp = "^(SEDENTARY|LIGHT|MODERATE|ACTIVE|VERY_ACTIVE)$")
    private String activityLevel;

    @NotNull
    @Pattern(regexp = "^(LOSE|MAINTAIN|GAIN)$")
    private String goalType;
}
