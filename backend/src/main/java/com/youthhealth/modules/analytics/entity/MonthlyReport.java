package com.youthhealth.modules.analytics.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MonthlyReport {
    private Long id;
    private Long userId;
    private String reportMonth;
    private LocalDate monthStartDate;
    private LocalDate monthEndDate;
    private String summaryJson;
    private String reportText;
    private LocalDateTime createdAt;
}
