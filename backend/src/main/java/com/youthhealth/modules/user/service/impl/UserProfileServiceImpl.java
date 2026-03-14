package com.youthhealth.modules.user.service.impl;

import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.auth.service.impl.AuthServiceImpl;
import com.youthhealth.modules.auth.vo.UserInfoVO;
import com.youthhealth.modules.user.dto.UpdateProfileRequest;
import com.youthhealth.modules.user.entity.SysUser;
import com.youthhealth.modules.user.entity.UserProfile;
import com.youthhealth.modules.user.mapper.SysRoleMapper;
import com.youthhealth.modules.user.mapper.SysUserMapper;
import com.youthhealth.modules.user.mapper.UserProfileMapper;
import com.youthhealth.modules.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoVO updateProfile(Long userId, UpdateProfileRequest request) {
        SysUser user = sysUserMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        UserProfile profile = userProfileMapper.findByUserId(userId);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
        }
        profile.setGender(request.getGender());
        profile.setBirthday(request.getBirthday());
        profile.setHeightCm(request.getHeightCm());
        profile.setWeightKg(request.getWeightKg());
        profile.setActivityLevel(request.getActivityLevel());
        profile.setGoalType(request.getGoalType());
        profile.setBmi(calcBmi(request.getHeightCm(), request.getWeightKg()));
        profile.setBmr(calcBmr(request.getGender(), request.getHeightCm(), request.getWeightKg(), request.getBirthday()));
        profile.setDailyCalorieTarget(calcDailyTarget(profile.getBmr(), request.getActivityLevel(), request.getGoalType()));

        if (profile.getId() == null) {
            userProfileMapper.insert(profile);
        } else {
            userProfileMapper.updateByUserId(profile);
        }
        List<String> roles = sysRoleMapper.findRoleCodesByUserId(userId);
        return AuthServiceImpl.toUserInfo(user, roles, profile);
    }

    private BigDecimal calcBmi(BigDecimal heightCm, BigDecimal weightKg) {
        BigDecimal heightMeter = heightCm.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return weightKg.divide(heightMeter.multiply(heightMeter), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcBmr(String gender, BigDecimal heightCm, BigDecimal weightKg, LocalDate birthday) {
        int age = Math.max(0, Period.between(birthday, LocalDate.now()).getYears());
        BigDecimal base = weightKg.multiply(BigDecimal.TEN)
                .add(heightCm.multiply(BigDecimal.valueOf(6.25)))
                .subtract(BigDecimal.valueOf(age * 5L));
        if ("MALE".equals(gender)) {
            return base.add(BigDecimal.valueOf(5)).setScale(2, RoundingMode.HALF_UP);
        }
        return base.subtract(BigDecimal.valueOf(161)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcDailyTarget(BigDecimal bmr, String activityLevel, String goalType) {
        BigDecimal factor = switch (activityLevel) {
            case "SEDENTARY" -> BigDecimal.valueOf(1.20);
            case "LIGHT" -> BigDecimal.valueOf(1.375);
            case "MODERATE" -> BigDecimal.valueOf(1.55);
            case "ACTIVE" -> BigDecimal.valueOf(1.725);
            case "VERY_ACTIVE" -> BigDecimal.valueOf(1.90);
            default -> BigDecimal.ONE;
        };
        BigDecimal maintenance = bmr.multiply(factor);
        BigDecimal adjust = switch (goalType) {
            case "LOSE" -> BigDecimal.valueOf(-300);
            case "GAIN" -> BigDecimal.valueOf(300);
            default -> BigDecimal.ZERO;
        };
        return maintenance.add(adjust).setScale(2, RoundingMode.HALF_UP);
    }
}
