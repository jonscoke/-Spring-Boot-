package com.youthhealth.modules.social.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SocialLike {
    private Long id;
    private Long postId;
    private Long userId;
    private LocalDateTime createdAt;
}
