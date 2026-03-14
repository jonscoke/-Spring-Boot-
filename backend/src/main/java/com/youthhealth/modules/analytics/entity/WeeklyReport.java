package com.youthhealth.modules.analytics.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WeeklyReport {
    private Long id;
    private Long userId;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private String summaryJson;
    private String reportText;
    private LocalDateTime createdAt;
}
