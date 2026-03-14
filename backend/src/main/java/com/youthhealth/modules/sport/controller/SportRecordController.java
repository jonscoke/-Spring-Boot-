package com.youthhealth.modules.sport.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.sport.dto.SportCreateRequest;
import com.youthhealth.modules.sport.dto.SportSyncMockRequest;
import com.youthhealth.modules.sport.dto.SportUpdateRequest;
import com.youthhealth.modules.sport.entity.SportRecord;
import com.youthhealth.modules.sport.service.SportRecordService;
import com.youthhealth.modules.sport.vo.SportPageVO;
import com.youthhealth.modules.sport.vo.SportSummaryVO;
import com.youthhealth.modules.sport.vo.SportSyncResultVO;
import com.youthhealth.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sports")
@RequiredArgsConstructor
public class SportRecordController {

    private final SportRecordService sportRecordService;

    @PostMapping
    public Result<SportRecord> create(@Valid @RequestBody SportCreateRequest request) {
        Long userId = currentUserId();
        return Result.success(sportRecordService.create(userId, request));
    }

    @PutMapping("/{id}")
    public Result<SportRecord> update(@PathVariable Long id, @Valid @RequestBody SportUpdateRequest request) {
        Long userId = currentUserId();
        return Result.success(sportRecordService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = currentUserId();
        sportRecordService.delete(userId, id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<SportPageVO> page(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = currentUserId();
        return Result.success(sportRecordService.page(userId, page, size, startDate, endDate));
    }

    @GetMapping("/date")
    public Result<List<SportRecord>> byDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate) {
        Long userId = currentUserId();
        return Result.success(sportRecordService.listByDate(userId, recordDate));
    }

    @GetMapping("/summary/daily")
    public Result<SportSummaryVO> daily(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = currentUserId();
        return Result.success(sportRecordService.dailySummary(userId, date));
    }

    @GetMapping("/summary/weekly")
    public Result<SportSummaryVO> weekly() {
        Long userId = currentUserId();
        return Result.success(sportRecordService.weeklySummary(userId));
    }

    @GetMapping("/summary/monthly")
    public Result<SportSummaryVO> monthly() {
        Long userId = currentUserId();
        return Result.success(sportRecordService.monthlySummary(userId));
    }

    @PostMapping("/sync/mock")
    public Result<SportSyncResultVO> syncMock(@RequestBody(required = false) SportSyncMockRequest request) {
        Long userId = currentUserId();
        return Result.success(sportRecordService.syncMock(userId, request == null ? new SportSyncMockRequest() : request));
    }

    private Long currentUserId() {
        Long userId = SecurityUtils.currentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
}
