package com.youthhealth.modules.auth.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoVO {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private List<String> roles;
    private UserProfileVO profile;
}
