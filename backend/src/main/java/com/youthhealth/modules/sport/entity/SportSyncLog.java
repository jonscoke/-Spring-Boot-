package com.youthhealth.modules.sport.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SportSyncLog {
    private Long id;
    private Long userId;
    private String sourceType;
    private Integer totalCount;
    private Integer insertedCount;
    private Integer duplicateCount;
    private String status;
    private String message;
    private LocalDateTime createdAt;
}
