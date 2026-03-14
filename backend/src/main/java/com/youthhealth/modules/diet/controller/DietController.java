package com.youthhealth.modules.diet.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.diet.dto.DietCreateRequest;
import com.youthhealth.modules.diet.service.DietService;
import com.youthhealth.modules.diet.vo.DietPageVO;
import com.youthhealth.modules.diet.vo.DietRecordVO;
import com.youthhealth.modules.diet.vo.DietSummaryVO;
import com.youthhealth.modules.diet.vo.NutritionRatioVO;
import com.youthhealth.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    @PostMapping
    public Result<DietRecordVO> create(@Valid @RequestBody DietCreateRequest request) {
        Long userId = currentUserId();
        return Result.success(dietService.create(userId, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = currentUserId();
        dietService.delete(userId, id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<DietPageVO> page(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = currentUserId();
        return Result.success(dietService.page(userId, page, size, startDate, endDate));
    }

    @GetMapping("/summary/daily")
    public Result<DietSummaryVO> dailySummary(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = currentUserId();
        return Result.success(dietService.dailySummary(userId, date));
    }

    @GetMapping("/summary/weekly")
    public Result<DietSummaryVO> weeklySummary() {
        Long userId = currentUserId();
        return Result.success(dietService.weeklySummary(userId));
    }

    @GetMapping("/nutrition-ratio")
    public Result<NutritionRatioVO> nutritionRatio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = currentUserId();
        return Result.success(dietService.nutritionRatio(userId, startDate, endDate));
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.currentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
}
