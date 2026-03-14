package com.youthhealth.modules.analytics.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdviceVO {
    private Long id;
    private String energyAdvice;
    private String dietAdvice;
    private String sportAdvice;
    private String adviceText;
    private String summaryJson;
    private String sourceType;
    private LocalDateTime createdAt;
}
