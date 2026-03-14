package com.youthhealth.modules.sport.service.impl;

import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.sport.dto.SportCreateRequest;
import com.youthhealth.modules.sport.dto.SportSyncMockRequest;
import com.youthhealth.modules.sport.dto.SportUpdateRequest;
import com.youthhealth.modules.sport.entity.SportRecord;
import com.youthhealth.modules.sport.entity.SportSyncLog;
import com.youthhealth.modules.sport.mapper.SportRecordMapper;
import com.youthhealth.modules.sport.service.SportRecordService;
import com.youthhealth.modules.sport.util.CaloriesCalculator;
import com.youthhealth.modules.sport.vo.SportPageVO;
import com.youthhealth.modules.sport.vo.SportSummaryRow;
import com.youthhealth.modules.sport.vo.SportSummaryVO;
import com.youthhealth.modules.sport.vo.SportSyncResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SportRecordServiceImpl implements SportRecordService {

    private final SportRecordMapper sportRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SportRecord create(Long userId, SportCreateRequest request) {
        SportRecord record = toRecord(userId, request);
        validateUniqueExternal(record, null);
        sportRecordMapper.insert(record);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SportRecord update(Long userId, Long id, SportUpdateRequest request) {
        SportRecord exists = sportRecordMapper.findByIdAndUserId(id, userId);
        if (exists == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        SportRecord record = toRecord(userId, request);
        record.setId(id);
        validateUniqueExternal(record, id);
        int updated = sportRecordMapper.updateByIdAndUserId(record);
        if (updated == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return sportRecordMapper.findByIdAndUserId(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long id) {
        int deleted = sportRecordMapper.deleteByIdAndUserId(id, userId);
        if (deleted == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }

    @Override
    public SportPageVO page(Long userId, Integer page, Integer size, LocalDate startDate, LocalDate endDate) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        int offset = (safePage - 1) * safeSize;
        Long total = sportRecordMapper.countByUserId(userId, startDate, endDate);
        List<SportRecord> records = total == null || total == 0
                ? List.of()
                : sportRecordMapper.pageByUserId(userId, startDate, endDate, offset, safeSize);
        return SportPageVO.builder()
                .total(total == null ? 0L : total)
                .page(safePage)
                .size(safeSize)
                .records(records)
                .build();
    }

    @Override
    public List<SportRecord> listByDate(Long userId, LocalDate recordDate) {
        if (recordDate == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST);
        }
        return sportRecordMapper.listByRecordDate(userId, recordDate);
    }

    @Override
    public SportSummaryVO dailySummary(Long userId, LocalDate date) {
        LocalDate target = date == null ? LocalDate.now() : date;
        return buildSummary(userId, target, target);
    }

    @Override
    public SportSummaryVO weeklySummary(Long userId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        return buildSummary(userId, start, end);
    }

    @Override
    public SportSummaryVO monthlySummary(Long userId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(29);
        return buildSummary(userId, start, end);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SportSyncResultVO syncMock(Long userId, SportSyncMockRequest request) {
        String sourceType = normalizeSourceType(request == null ? null : request.getSourceType());
        List<SportRecord> mockRecords = buildMockRecords(userId, sourceType);
        int insertedCount = 0;
        int duplicateCount = 0;
        for (SportRecord mock : mockRecords) {
            SportRecord exists = sportRecordMapper.findByUniqueSource(userId, sourceType, mock.getExternalId());
            if (exists != null) {
                duplicateCount++;
                continue;
            }
            sportRecordMapper.insert(mock);
            insertedCount++;
        }

        SportSyncLog log = new SportSyncLog();
        log.setUserId(userId);
        log.setSourceType(sourceType);
        log.setTotalCount(mockRecords.size());
        log.setInsertedCount(insertedCount);
        log.setDuplicateCount(duplicateCount);
        log.setStatus("SUCCESS");
        log.setMessage("mock sync finished");
        sportRecordMapper.insertSyncLog(log);

        return SportSyncResultVO.builder()
                .sourceType(sourceType)
                .totalMockCount(mockRecords.size())
                .insertedCount(insertedCount)
                .duplicateCount(duplicateCount)
                .mockSamples(mockRecords)
                .build();
    }

    private SportSummaryVO buildSummary(Long userId, LocalDate start, LocalDate end) {
        SportSummaryRow row = sportRecordMapper.summaryByDateRange(userId, start, end);
        if (row == null) {
            row = new SportSummaryRow();
        }
        return SportSummaryVO.builder()
                .startDate(start)
                .endDate(end)
                .recordCount(row.getRecordCount() == null ? 0L : row.getRecordCount())
                .totalSteps(row.getTotalSteps() == null ? 0 : row.getTotalSteps())
                .totalDistanceKm(row.getTotalDistanceKm() == null ? BigDecimal.ZERO : row.getTotalDistanceKm())
                .totalDurationMin(row.getTotalDurationMin() == null ? 0 : row.getTotalDurationMin())
                .totalCaloriesBurned(row.getTotalCaloriesBurned() == null ? BigDecimal.ZERO : row.getTotalCaloriesBurned())
                .build();
    }

    private SportRecord toRecord(Long userId, SportCreateRequest request) {
        SportRecord record = new SportRecord();
        record.setUserId(userId);
        record.setSportType(request.getSportType().trim().toUpperCase(Locale.ROOT));
        record.setSteps(request.getSteps());
        record.setDistanceKm(request.getDistanceKm().setScale(2, RoundingMode.HALF_UP));
        record.setDurationMin(request.getDurationMin());
        record.setPace(calculatePace(request.getPace(), request.getDistanceKm(), request.getDurationMin()));
        record.setCaloriesBurned(calculateCalories(request.getCaloriesBurned(), request.getSportType(), request.getDurationMin()));
        record.setSourceType(normalizeSourceType(request.getSourceType()));
        record.setExternalId(StringUtils.hasText(request.getExternalId()) ? request.getExternalId().trim() : null);
        record.setRecordDate(request.getRecordDate());
        return record;
    }

    private SportRecord toRecord(Long userId, SportUpdateRequest request) {
        SportCreateRequest createRequest = new SportCreateRequest();
        createRequest.setSportType(request.getSportType());
        createRequest.setSteps(request.getSteps());
        createRequest.setDistanceKm(request.getDistanceKm());
        createRequest.setDurationMin(request.getDurationMin());
        createRequest.setPace(request.getPace());
        createRequest.setCaloriesBurned(request.getCaloriesBurned());
        createRequest.setSourceType(request.getSourceType());
        createRequest.setExternalId(request.getExternalId());
        createRequest.setRecordDate(request.getRecordDate());
        return toRecord(userId, createRequest);
    }

    private BigDecimal calculatePace(BigDecimal pace, BigDecimal distanceKm, Integer durationMin) {
        if (pace != null && pace.compareTo(BigDecimal.ZERO) > 0) {
            return pace.setScale(2, RoundingMode.HALF_UP);
        }
        if (distanceKm == null || durationMin == null || distanceKm.compareTo(BigDecimal.ZERO) <= 0 || durationMin <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(durationMin)
                .divide(distanceKm, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateCalories(BigDecimal caloriesBurned, String sportType, Integer durationMin) {
        if (caloriesBurned != null && caloriesBurned.compareTo(BigDecimal.ZERO) > 0) {
            return caloriesBurned.setScale(2, RoundingMode.HALF_UP);
        }
        return CaloriesCalculator.calculate(sportType, durationMin);
    }

    private String normalizeSourceType(String sourceType) {
        if (!StringUtils.hasText(sourceType)) {
            return "MANUAL";
        }
        return sourceType.trim().toUpperCase(Locale.ROOT);
    }

    private void validateUniqueExternal(SportRecord record, Long currentId) {
        if (!StringUtils.hasText(record.getExternalId())) {
            return;
        }
        SportRecord duplicate = sportRecordMapper.findByUniqueSource(record.getUserId(), record.getSourceType(), record.getExternalId());
        if (duplicate == null) {
            return;
        }
        if (currentId == null || !currentId.equals(duplicate.getId())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "externalId duplicated under same sourceType");
        }
    }

    private List<SportRecord> buildMockRecords(Long userId, String sourceType) {
        LocalDate today = LocalDate.now();
        List<SportRecord> list = new ArrayList<>();
        list.add(mock(userId, "RUN", 8520, BigDecimal.valueOf(6.40), 42, sourceType, "MOCK-001", today));
        list.add(mock(userId, "WALK", 6230, BigDecimal.valueOf(4.10), 55, sourceType, "MOCK-002", today.minusDays(1)));
        list.add(mock(userId, "CYCLE", 0, BigDecimal.valueOf(12.80), 38, sourceType, "MOCK-003", today.minusDays(2)));
        return list;
    }

    private SportRecord mock(Long userId, String sportType, Integer steps, BigDecimal distanceKm,
                             Integer durationMin, String sourceType, String externalId, LocalDate recordDate) {
        SportRecord record = new SportRecord();
        record.setUserId(userId);
        record.setSportType(sportType);
        record.setSteps(steps);
        record.setDistanceKm(distanceKm.setScale(2, RoundingMode.HALF_UP));
        record.setDurationMin(durationMin);
        record.setPace(calculatePace(null, distanceKm, durationMin));
        record.setCaloriesBurned(CaloriesCalculator.calculate(sportType, durationMin));
        record.setSourceType(sourceType);
        record.setExternalId(externalId);
        record.setRecordDate(recordDate);
        return record;
    }
}
