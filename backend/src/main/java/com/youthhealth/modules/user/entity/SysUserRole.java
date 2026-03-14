package com.youthhealth.modules.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysUserRole {
    private Long id;
    private Long userId;
    private Long roleId;
    private LocalDateTime createdAt;
}
