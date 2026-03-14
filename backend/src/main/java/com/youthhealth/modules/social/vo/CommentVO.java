package com.youthhealth.modules.social.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentVO {
    private Long id;
    private Long postId;
    private Long userId;
    private String authorNickname;
    private String content;
    private LocalDateTime createdAt;
}
