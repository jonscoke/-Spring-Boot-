package com.youthhealth.modules.analytics.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportVO {
    private Long id;
    private String reportType;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String title;
    private String summaryJson;
    private String reportText;
    private LocalDateTime createdAt;
}
