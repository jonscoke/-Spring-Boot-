package com.youthhealth.modules.analytics.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.analytics.service.StatsService;
import com.youthhealth.modules.analytics.vo.StatsDashboardVO;
import com.youthhealth.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/dashboard")
    public Result<StatsDashboardVO> dashboard() {
        Long userId = currentUserId();
        return Result.success(statsService.dashboard(userId));
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.currentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
}
