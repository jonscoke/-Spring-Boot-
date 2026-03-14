package com.youthhealth.modules.analytics.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdviceLog {
    private Long id;
    private Long userId;
    private String energyAdvice;
    private String dietAdvice;
    private String sportAdvice;
    private String adviceText;
    private String summaryJson;
    private String sourceType;
    private LocalDateTime createdAt;
}
