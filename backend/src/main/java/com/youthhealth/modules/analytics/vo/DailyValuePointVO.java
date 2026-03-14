package com.youthhealth.modules.analytics.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class DailyValuePointVO {
    private LocalDate date;
    private BigDecimal value;
}
