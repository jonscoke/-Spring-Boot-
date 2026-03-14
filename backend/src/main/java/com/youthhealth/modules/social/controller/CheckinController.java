package com.youthhealth.modules.social.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.social.dto.CheckinCreateRequest;
import com.youthhealth.modules.social.entity.UserCheckin;
import com.youthhealth.modules.social.service.CheckinService;
import com.youthhealth.modules.social.vo.PageVO;
import com.youthhealth.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    @PostMapping
    public Result<UserCheckin> create(@Valid @RequestBody CheckinCreateRequest request) {
        return Result.success(checkinService.create(currentUserId(), request));
    }

    @GetMapping("/page")
    public Result<PageVO<UserCheckin>> page(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(checkinService.page(currentUserId(), page, size));
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.currentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
}
