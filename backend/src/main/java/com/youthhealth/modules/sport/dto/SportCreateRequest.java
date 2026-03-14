package com.youthhealth.modules.sport.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SportCreateRequest {
    @NotBlank
    private String sportType;

    @NotNull
    @Min(0)
    private Integer steps;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal distanceKm;

    @NotNull
    @Min(0)
    private Integer durationMin;

    @DecimalMin(value = "0.00")
    private BigDecimal pace;

    @DecimalMin(value = "0.00")
    private BigDecimal caloriesBurned;

    @NotBlank
    private String sourceType;

    private String externalId;

    @NotNull
    private LocalDate recordDate;
}
