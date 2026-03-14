package com.youthhealth.modules.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysRole {
    private Long id;
    private String roleCode;
    private String roleName;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
