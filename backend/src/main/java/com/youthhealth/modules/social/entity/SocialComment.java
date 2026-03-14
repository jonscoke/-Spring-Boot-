package com.youthhealth.modules.social.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SocialComment {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
}
