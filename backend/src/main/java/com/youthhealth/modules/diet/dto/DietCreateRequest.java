package com.youthhealth.modules.diet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DietCreateRequest {
    @NotBlank
    private String mealType;

    @NotNull
    private LocalDate recordDate;

    private String remark;

    @Valid
    @NotEmpty
    private List<DietRecordItemRequest> items;
}
