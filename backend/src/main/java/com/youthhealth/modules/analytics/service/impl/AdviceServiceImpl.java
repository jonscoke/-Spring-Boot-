package com.youthhealth.modules.analytics.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youthhealth.modules.analytics.entity.AdviceLog;
import com.youthhealth.modules.analytics.mapper.AdviceMapper;
import com.youthhealth.modules.analytics.service.AdviceService;
import com.youthhealth.modules.analytics.vo.AdviceVO;
import com.youthhealth.modules.diet.mapper.DietRecordMapper;
import com.youthhealth.modules.diet.vo.DietSummaryRow;
import com.youthhealth.modules.sport.mapper.SportRecordMapper;
import com.youthhealth.modules.sport.vo.SportSummaryRow;
import com.youthhealth.modules.user.entity.UserProfile;
import com.youthhealth.modules.user.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdviceServiceImpl implements AdviceService {

    private final AdviceMapper adviceMapper;
    private final DietRecordMapper dietRecordMapper;
    private final SportRecordMapper sportRecordMapper;
    private final UserProfileMapper userProfileMapper;
    private final RuleAdviceGenerator ruleAdviceGenerator;
    private final ObjectMapper objectMapper;

    @Override
    public AdviceVO latest(Long userId) {
        AdviceLog latest = adviceMapper.findLatestByUserId(userId);
        return latest == null ? null : toVO(latest);
    }

    @Override
    public AdviceVO generate(Long userId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        DietSummaryRow dietSummary = dietRecordMapper.summaryByDateRange(userId, start, end);
        SportSummaryRow sportSummary = sportRecordMapper.summaryByDateRange(userId, start, end);
        UserProfile profile = userProfileMapper.findByUserId(userId);

        BigDecimal intake7d = safe(dietSummary == null ? null : dietSummary.getTotalCalories());
        BigDecimal burn7d = safe(sportSummary == null ? null : sportSummary.getTotalCaloriesBurned());
        BigDecimal avgNet = intake7d.subtract(burn7d)
                .divide(BigDecimal.valueOf(7), 2, RoundingMode.HALF_UP);

        BigDecimal protein = safe(dietSummary == null ? null : dietSummary.getTotalProtein());
        BigDecimal fat = safe(dietSummary == null ? null : dietSummary.getTotalFat());
        BigDecimal carb = safe(dietSummary == null ? null : dietSummary.getTotalCarb());
        BigDecimal proteinCalories = protein.multiply(BigDecimal.valueOf(4));
        BigDecimal fatCalories = fat.multiply(BigDecimal.valueOf(9));
        BigDecimal carbCalories = carb.multiply(BigDecimal.valueOf(4));
        BigDecimal macroTotalCalories = proteinCalories.add(fatCalories).add(carbCalories);

        RuleAdviceGenerator.AdviceInput adviceInput = RuleAdviceGenerator.AdviceInput.builder()
                .dailyCalorieTarget(profile == null ? null : profile.getDailyCalorieTarget())
                .avgNetCalories(avgNet)
                .proteinCalories(proteinCalories)
                .fatCalories(fatCalories)
                .carbCalories(carbCalories)
                .totalMacroCalories(macroTotalCalories)
                .totalDurationMin(sportSummary == null || sportSummary.getTotalDurationMin() == null ? 0 : sportSummary.getTotalDurationMin())
                .totalSteps(sportSummary == null || sportSummary.getTotalSteps() == null ? 0 : sportSummary.getTotalSteps())
                .build();
        RuleAdviceGenerator.AdviceText adviceText = ruleAdviceGenerator.generate(adviceInput);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("periodStart", start);
        summary.put("periodEnd", end);
        summary.put("bmi", profile == null ? null : profile.getBmi());
        summary.put("bmr", profile == null ? null : profile.getBmr());
        summary.put("dailyCalorieTarget", profile == null ? null : profile.getDailyCalorieTarget());
        summary.put("intakeCalories7d", intake7d);
        summary.put("burnCalories7d", burn7d);
        summary.put("avgNetCalories", avgNet);
        summary.put("proteinG7d", protein);
        summary.put("fatG7d", fat);
        summary.put("carbG7d", carb);

        AdviceLog log = new AdviceLog();
        log.setUserId(userId);
        log.setEnergyAdvice(adviceText.getEnergyAdvice());
        log.setDietAdvice(adviceText.getDietAdvice());
        log.setSportAdvice(adviceText.getSportAdvice());
        log.setAdviceText(adviceText.getAdviceText());
        log.setSummaryJson(toJson(summary));
        // Current default is rule engine; LLMAdviceService is reserved as an extension point.
        log.setSourceType("RULE");
        adviceMapper.insert(log);
        return toVO(log);
    }

    private AdviceVO toVO(AdviceLog adviceLog) {
        return AdviceVO.builder()
                .id(adviceLog.getId())
                .energyAdvice(adviceLog.getEnergyAdvice())
                .dietAdvice(adviceLog.getDietAdvice())
                .sportAdvice(adviceLog.getSportAdvice())
                .adviceText(adviceLog.getAdviceText())
                .summaryJson(adviceLog.getSummaryJson())
                .sourceType(adviceLog.getSourceType())
                .createdAt(adviceLog.getCreatedAt())
                .build();
    }

    private String toJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception ex) {
            return "{}";
        }
    }

    private BigDecimal safe(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
