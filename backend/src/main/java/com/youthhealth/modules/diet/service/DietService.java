package com.youthhealth.modules.diet.service;

import com.youthhealth.modules.diet.dto.DietCreateRequest;
import com.youthhealth.modules.diet.vo.DietPageVO;
import com.youthhealth.modules.diet.vo.DietRecordVO;
import com.youthhealth.modules.diet.vo.DietSummaryVO;
import com.youthhealth.modules.diet.vo.NutritionRatioVO;

import java.time.LocalDate;

public interface DietService {
    DietRecordVO create(Long userId, DietCreateRequest request);

    void delete(Long userId, Long id);

    DietPageVO page(Long userId, Integer page, Integer size, LocalDate startDate, LocalDate endDate);

    DietSummaryVO dailySummary(Long userId, LocalDate date);

    DietSummaryVO weeklySummary(Long userId);

    NutritionRatioVO nutritionRatio(Long userId, LocalDate startDate, LocalDate endDate);
}
