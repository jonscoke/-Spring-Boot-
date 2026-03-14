package com.youthhealth.modules.analytics.service.impl;

import com.youthhealth.modules.analytics.mapper.StatsMapper;
import com.youthhealth.modules.analytics.service.StatsService;
import com.youthhealth.modules.analytics.vo.DietTrendPointVO;
import com.youthhealth.modules.analytics.vo.SportTrendPointVO;
import com.youthhealth.modules.analytics.vo.StatsDashboardVO;
import com.youthhealth.modules.analytics.vo.WeightTrendPointVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsMapper statsMapper;

    @Override
    public StatsDashboardVO dashboard(Long userId) {
        LocalDate today = LocalDate.now();
        BigDecimal todayIntake = safe(statsMapper.sumDietCaloriesByDate(userId, today));
        BigDecimal todayBurn = safe(statsMapper.sumSportCaloriesByDate(userId, today));
        BigDecimal todayGap = todayIntake.subtract(todayBurn).setScale(2, RoundingMode.HALF_UP);

        LocalDate start = today.minusDays(6);
        List<SportTrendPointVO> sportTrend = fillSportTrend(start, today, statsMapper.sportTrendByDateRange(userId, start, today));
        List<DietTrendPointVO> dietTrend = fillDietTrend(start, today, statsMapper.dietTrendByDateRange(userId, start, today));
        List<WeightTrendPointVO> weightTrend = buildWeightTrend(userId);

        return StatsDashboardVO.builder()
                .todayIntakeCalories(todayIntake)
                .todayBurnCalories(todayBurn)
                .todayEnergyGap(todayGap)
                .sportTrend7d(sportTrend)
                .dietTrend7d(dietTrend)
                .weightTrend(weightTrend)
                .build();
    }

    private List<SportTrendPointVO> fillSportTrend(LocalDate start, LocalDate end, List<SportTrendPointVO> rows) {
        Map<LocalDate, SportTrendPointVO> rowMap = new HashMap<>();
        for (SportTrendPointVO row : rows) {
            rowMap.put(row.getDate(), row);
        }
        List<SportTrendPointVO> result = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            SportTrendPointVO row = rowMap.get(d);
            if (row == null) {
                row = new SportTrendPointVO();
                row.setDate(d);
                row.setCaloriesBurned(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                row.setSteps(0);
                row.setDurationMin(0);
            } else {
                row.setCaloriesBurned(safe(row.getCaloriesBurned()));
                row.setSteps(row.getSteps() == null ? 0 : row.getSteps());
                row.setDurationMin(row.getDurationMin() == null ? 0 : row.getDurationMin());
            }
            result.add(row);
        }
        return result;
    }

    private List<DietTrendPointVO> fillDietTrend(LocalDate start, LocalDate end, List<DietTrendPointVO> rows) {
        Map<LocalDate, DietTrendPointVO> rowMap = new HashMap<>();
        for (DietTrendPointVO row : rows) {
            rowMap.put(row.getDate(), row);
        }
        List<DietTrendPointVO> result = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            DietTrendPointVO row = rowMap.get(d);
            if (row == null) {
                row = new DietTrendPointVO();
                row.setDate(d);
                row.setCaloriesIntake(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                row.setProtein(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                row.setFat(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                row.setCarb(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            } else {
                row.setCaloriesIntake(safe(row.getCaloriesIntake()));
                row.setProtein(safe(row.getProtein()));
                row.setFat(safe(row.getFat()));
                row.setCarb(safe(row.getCarb()));
            }
            result.add(row);
        }
        return result;
    }

    private List<WeightTrendPointVO> buildWeightTrend(Long userId) {
        BigDecimal weight = statsMapper.latestWeightKg(userId);
        LocalDateTime updatedAt = statsMapper.latestWeightUpdatedAt(userId);
        if (weight == null) {
            return List.of();
        }
        LocalDate date = updatedAt == null ? LocalDate.now() : updatedAt.toLocalDate();
        return List.of(WeightTrendPointVO.builder()
                .date(date)
                .weightKg(weight.setScale(2, RoundingMode.HALF_UP))
                .build());
    }

    private BigDecimal safe(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
