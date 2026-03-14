package com.youthhealth.modules.social.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalCreateRequest {
    @NotBlank
    private String goalType;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal goalValue;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private String status;
}
