package com.youthhealth.modules.sport.service;

import com.youthhealth.modules.sport.dto.SportCreateRequest;
import com.youthhealth.modules.sport.dto.SportSyncMockRequest;
import com.youthhealth.modules.sport.dto.SportUpdateRequest;
import com.youthhealth.modules.sport.entity.SportRecord;
import com.youthhealth.modules.sport.vo.SportPageVO;
import com.youthhealth.modules.sport.vo.SportSummaryVO;
import com.youthhealth.modules.sport.vo.SportSyncResultVO;

import java.time.LocalDate;
import java.util.List;

public interface SportRecordService {
    SportRecord create(Long userId, SportCreateRequest request);

    SportRecord update(Long userId, Long id, SportUpdateRequest request);

    void delete(Long userId, Long id);

    SportPageVO page(Long userId, Integer page, Integer size, LocalDate startDate, LocalDate endDate);

    List<SportRecord> listByDate(Long userId, LocalDate recordDate);

    SportSummaryVO dailySummary(Long userId, LocalDate date);

    SportSummaryVO weeklySummary(Long userId);

    SportSummaryVO monthlySummary(Long userId);

    SportSyncResultVO syncMock(Long userId, SportSyncMockRequest request);
}
