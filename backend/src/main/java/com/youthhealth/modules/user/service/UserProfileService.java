package com.youthhealth.modules.user.service;

import com.youthhealth.modules.auth.vo.UserInfoVO;
import com.youthhealth.modules.user.dto.UpdateProfileRequest;

public interface UserProfileService {
    UserInfoVO updateProfile(Long userId, UpdateProfileRequest request);
}
