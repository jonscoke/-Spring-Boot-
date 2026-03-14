package com.youthhealth.modules.analytics.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.analytics.service.AdviceService;
import com.youthhealth.modules.analytics.vo.AdviceVO;
import com.youthhealth.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advice")
@RequiredArgsConstructor
public class AdviceController {

    private final AdviceService adviceService;

    @GetMapping("/latest")
    public Result<AdviceVO> latest() {
        Long userId = currentUserId();
        return Result.success(adviceService.latest(userId));
    }

    @PostMapping("/generate")
    public Result<AdviceVO> generate() {
        Long userId = currentUserId();
        return Result.success(adviceService.generate(userId));
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.currentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
}
