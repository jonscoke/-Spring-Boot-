package com.youthhealth.modules.auth.service;

import com.youthhealth.modules.auth.dto.LoginRequest;
import com.youthhealth.modules.auth.dto.RegisterRequest;
import com.youthhealth.modules.auth.vo.LoginVO;
import com.youthhealth.modules.auth.vo.UserInfoVO;

public interface AuthService {
    void register(RegisterRequest request);

    LoginVO login(LoginRequest request);

    UserInfoVO me(Long userId);
}
