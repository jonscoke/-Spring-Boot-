package com.youthhealth.modules.social.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostPageItemVO {
    private Long id;
    private Long userId;
    private String authorNickname;
    private String content;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean liked;
    private LocalDateTime createdAt;
}
