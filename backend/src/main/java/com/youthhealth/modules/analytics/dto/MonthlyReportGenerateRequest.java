package com.youthhealth.modules.analytics.dto;

import lombok.Data;

@Data
public class MonthlyReportGenerateRequest {
    // format: yyyy-MM
    private String reportMonth;
}
