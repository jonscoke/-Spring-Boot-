package com.youthhealth.modules.social.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.social.dto.GoalCreateRequest;
import com.youthhealth.modules.social.dto.GoalUpdateRequest;
import com.youthhealth.modules.social.entity.UserGoal;
import com.youthhealth.modules.social.service.GoalService;
import com.youthhealth.modules.social.vo.PageVO;
import com.youthhealth.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public Result<UserGoal> create(@Valid @RequestBody GoalCreateRequest request) {
        return Result.success(goalService.create(currentUserId(), request));
    }

    @PutMapping("/{id}")
    public Result<UserGoal> update(@PathVariable Long id, @Valid @RequestBody GoalUpdateRequest request) {
        return Result.success(goalService.update(currentUserId(), id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        goalService.delete(currentUserId(), id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageVO<UserGoal>> page(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(goalService.page(currentUserId(), page, size));
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.currentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
}
