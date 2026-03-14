package com.youthhealth.modules.social.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SocialPost {
    private Long id;
    private Long userId;
    private String content;
    private Integer likeCount;
    private Integer commentCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
