package com.youthhealth.modules.analytics.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class WeightTrendPointVO {
    private LocalDate date;
    private BigDecimal weightKg;
}
