package com.youthhealth.modules.social.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserCheckin {
    private Long id;
    private Long userId;
    private LocalDate checkinDate;
    private String content;
    private Integer moodScore;
    private Long goalId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
