package com.youthhealth.modules.diet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DietRecordItemRequest {
    @NotNull
    private Long foodItemId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal intakeGram;
}
