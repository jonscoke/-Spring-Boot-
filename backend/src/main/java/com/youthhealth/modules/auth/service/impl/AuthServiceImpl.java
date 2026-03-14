package com.youthhealth.modules.auth.service.impl;

import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.auth.dto.LoginRequest;
import com.youthhealth.modules.auth.dto.RegisterRequest;
import com.youthhealth.modules.auth.service.AuthService;
import com.youthhealth.modules.auth.vo.LoginVO;
import com.youthhealth.modules.auth.vo.UserInfoVO;
import com.youthhealth.modules.auth.vo.UserProfileVO;
import com.youthhealth.modules.user.entity.SysRole;
import com.youthhealth.modules.user.entity.SysUser;
import com.youthhealth.modules.user.entity.SysUserRole;
import com.youthhealth.modules.user.entity.UserProfile;
import com.youthhealth.modules.user.mapper.SysRoleMapper;
import com.youthhealth.modules.user.mapper.SysUserMapper;
import com.youthhealth.modules.user.mapper.SysUserRoleMapper;
import com.youthhealth.modules.user.mapper.UserProfileMapper;
import com.youthhealth.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final UserProfileMapper userProfileMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        if (sysUserMapper.findByUsername(request.getUsername()) != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setStatus(1);
        sysUserMapper.insert(user);

        bindRole(user.getId(), "USER");
    }

    @Override
    public LoginVO login(LoginRequest request) {
        SysUser user = sysUserMapper.findByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash()) || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }
        List<String> roleCodes = sysRoleMapper.findRoleCodesByUserId(user.getId());
        String token = jwtTokenService.generateToken(user.getId(), user.getUsername(), roleCodes);
        return LoginVO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenService.getExpireSeconds())
                .user(toUserInfo(user, roleCodes, userProfileMapper.findByUserId(user.getId())))
                .build();
    }

    @Override
    public UserInfoVO me(Long userId) {
        SysUser user = sysUserMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        List<String> roleCodes = sysRoleMapper.findRoleCodesByUserId(userId);
        UserProfile profile = userProfileMapper.findByUserId(userId);
        return toUserInfo(user, roleCodes, profile);
    }

    private void bindRole(Long userId, String roleCode) {
        SysRole role = sysRoleMapper.findByRoleCode(roleCode);
        if (role == null) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), "Role not found: " + roleCode);
        }
        if (sysUserRoleMapper.countByUserIdAndRoleId(userId, role.getId()) > 0) {
            return;
        }
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());
        sysUserRoleMapper.insert(userRole);
    }

    public static UserInfoVO toUserInfo(SysUser user, List<String> roles, UserProfile profile) {
        UserProfileVO profileVO = null;
        if (profile != null) {
            profileVO = UserProfileVO.builder()
                    .gender(profile.getGender())
                    .birthday(profile.getBirthday())
                    .heightCm(profile.getHeightCm())
                    .weightKg(profile.getWeightKg())
                    .activityLevel(profile.getActivityLevel())
                    .goalType(profile.getGoalType())
                    .bmi(profile.getBmi())
                    .bmr(profile.getBmr())
                    .dailyCalorieTarget(profile.getDailyCalorieTarget())
                    .build();
        }
        return UserInfoVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .roles(roles)
                .profile(profileVO)
                .build();
    }
}
