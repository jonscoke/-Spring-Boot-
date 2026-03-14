package com.youthhealth.modules.sport.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;

public final class CaloriesCalculator {
    private CaloriesCalculator() {
    }

    private static final Map<String, BigDecimal> MET_MAP = Map.of(
            "WALK", BigDecimal.valueOf(3.5),
            "RUN", BigDecimal.valueOf(7.5),
            "CYCLE", BigDecimal.valueOf(6.8),
            "SWIM", BigDecimal.valueOf(8.0),
            "BALL", BigDecimal.valueOf(6.0)
    );

    // rule: calories = durationHour * MET * 60 (assuming avg 60kg person)
    public static BigDecimal calculate(String sportType, Integer durationMin) {
        if (durationMin == null || durationMin <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        String key = sportType == null ? "" : sportType.trim().toUpperCase(Locale.ROOT);
        BigDecimal met = MET_MAP.getOrDefault(key, BigDecimal.valueOf(5.0));
        BigDecimal durationHour = BigDecimal.valueOf(durationMin).divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);
        return durationHour.multiply(met).multiply(BigDecimal.valueOf(60)).setScale(2, RoundingMode.HALF_UP);
    }
}
