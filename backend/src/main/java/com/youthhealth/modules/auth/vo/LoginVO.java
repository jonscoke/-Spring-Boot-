package com.youthhealth.modules.auth.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {
    private String token;
    private String tokenType;
    private long expiresIn;
    private UserInfoVO user;
}
