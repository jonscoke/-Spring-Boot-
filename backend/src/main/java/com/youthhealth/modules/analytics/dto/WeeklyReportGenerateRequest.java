package com.youthhealth.modules.analytics.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WeeklyReportGenerateRequest {
    private LocalDate weekStartDate;
}
