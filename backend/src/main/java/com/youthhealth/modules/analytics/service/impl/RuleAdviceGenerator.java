package com.youthhealth.modules.analytics.service.impl;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class RuleAdviceGenerator {

    public AdviceText generate(AdviceInput input) {
        String energyAdvice = buildEnergyAdvice(input);
        String dietAdvice = buildDietAdvice(input);
        String sportAdvice = buildSportAdvice(input);
        String adviceText = "Energy Balance: " + energyAdvice + "\n"
                + "Diet Advice: " + dietAdvice + "\n"
                + "Sport Advice: " + sportAdvice;
        return AdviceText.builder()
                .energyAdvice(energyAdvice)
                .dietAdvice(dietAdvice)
                .sportAdvice(sportAdvice)
                .adviceText(adviceText)
                .build();
    }

    private String buildEnergyAdvice(AdviceInput in) {
        BigDecimal avgNet = in.getAvgNetCalories();
        BigDecimal target = in.getDailyCalorieTarget();
        if (target == null) {
            if (avgNet.compareTo(BigDecimal.ZERO) > 150) {
                return "Your recent net energy is high; reduce intake slightly or increase activity.";
            }
            if (avgNet.compareTo(BigDecimal.ZERO) < -150) {
                return "Your recent net energy is low; increase quality calories to avoid under-fueling.";
            }
            return "Your recent energy balance is relatively stable.";
        }
        BigDecimal diff = avgNet.subtract(target);
        if (diff.compareTo(BigDecimal.valueOf(200)) > 0) {
            return "Average net energy is above target. Consider reducing 200-300 kcal/day or adding light exercise.";
        }
        if (diff.compareTo(BigDecimal.valueOf(-200)) < 0) {
            return "Average net energy is below target. Consider adding balanced meals and recovery snacks.";
        }
        return "Average net energy is close to your target. Keep current routine.";
    }

    private String buildDietAdvice(AdviceInput in) {
        if (in.getTotalMacroCalories().compareTo(BigDecimal.ZERO) <= 0) {
            return "Insufficient diet records in the last 7 days. Keep logging meals for better analysis.";
        }
        BigDecimal proteinRatio = ratio(in.getProteinCalories(), in.getTotalMacroCalories());
        BigDecimal fatRatio = ratio(in.getFatCalories(), in.getTotalMacroCalories());
        BigDecimal carbRatio = ratio(in.getCarbCalories(), in.getTotalMacroCalories());
        return "Macro ratio (P/F/C): "
                + percent(proteinRatio) + "/" + percent(fatRatio) + "/" + percent(carbRatio)
                + ". Aim around 20-30% protein, 20-30% fat, and 40-55% carbs.";
    }

    private String buildSportAdvice(AdviceInput in) {
        if (in.getTotalDurationMin() < 90) {
            return "Weekly exercise duration is low. Target at least 150 minutes moderate activity.";
        }
        if (in.getTotalSteps() < 50000) {
            return "Increase daily walking volume toward 7000-10000 steps.";
        }
        return "Exercise volume is good. Keep consistency and include 2-3 strength sessions.";
    }

    private BigDecimal ratio(BigDecimal part, BigDecimal total) {
        return part.divide(total, 4, RoundingMode.HALF_UP);
    }

    private String percent(BigDecimal value) {
        return value.multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP) + "%";
    }

    @Data
    @Builder
    public static class AdviceInput {
        private BigDecimal dailyCalorieTarget;
        private BigDecimal avgNetCalories;
        private BigDecimal proteinCalories;
        private BigDecimal fatCalories;
        private BigDecimal carbCalories;
        private BigDecimal totalMacroCalories;
        private Integer totalDurationMin;
        private Integer totalSteps;
    }

    @Data
    @Builder
    public static class AdviceText {
        private String energyAdvice;
        private String dietAdvice;
        private String sportAdvice;
        private String adviceText;
    }
}
