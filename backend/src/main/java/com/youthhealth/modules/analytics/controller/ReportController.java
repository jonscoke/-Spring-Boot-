package com.youthhealth.modules.analytics.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.analytics.dto.MonthlyReportGenerateRequest;
import com.youthhealth.modules.analytics.dto.WeeklyReportGenerateRequest;
import com.youthhealth.modules.analytics.service.ReportService;
import com.youthhealth.modules.analytics.vo.ReportPageVO;
import com.youthhealth.modules.analytics.vo.ReportVO;
import com.youthhealth.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/weekly/generate")
    public Result<ReportVO> generateWeekly(@RequestBody(required = false) WeeklyReportGenerateRequest request) {
        Long userId = currentUserId();
        return Result.success(reportService.generateWeekly(userId, request == null ? new WeeklyReportGenerateRequest() : request));
    }

    @PostMapping("/monthly/generate")
    public Result<ReportVO> generateMonthly(@RequestBody(required = false) MonthlyReportGenerateRequest request) {
        Long userId = currentUserId();
        return Result.success(reportService.generateMonthly(userId, request == null ? new MonthlyReportGenerateRequest() : request));
    }

    @GetMapping("/page")
    public Result<ReportPageVO> page(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String reportType) {
        Long userId = currentUserId();
        return Result.success(reportService.page(userId, page, size, reportType));
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.currentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
}
