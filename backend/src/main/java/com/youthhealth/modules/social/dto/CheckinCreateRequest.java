package com.youthhealth.modules.social.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CheckinCreateRequest {
    @NotNull
    private LocalDate checkinDate;

    @NotBlank
    private String content;

    @Min(1)
    @Max(5)
    private Integer moodScore;

    private Long goalId;
}
