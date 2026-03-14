package com.youthhealth.modules.social.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserGoal {
    private Long id;
    private Long userId;
    private String goalType;
    private BigDecimal goalValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
