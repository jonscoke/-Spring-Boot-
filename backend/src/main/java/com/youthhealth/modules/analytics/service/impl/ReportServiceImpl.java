package com.youthhealth.modules.analytics.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.analytics.dto.MonthlyReportGenerateRequest;
import com.youthhealth.modules.analytics.dto.WeeklyReportGenerateRequest;
import com.youthhealth.modules.analytics.entity.MonthlyReport;
import com.youthhealth.modules.analytics.entity.WeeklyReport;
import com.youthhealth.modules.analytics.mapper.AdviceMapper;
import com.youthhealth.modules.analytics.mapper.ReportMapper;
import com.youthhealth.modules.analytics.mapper.StatsMapper;
import com.youthhealth.modules.analytics.service.ReportService;
import com.youthhealth.modules.analytics.vo.AdviceVO;
import com.youthhealth.modules.analytics.vo.DietTrendPointVO;
import com.youthhealth.modules.analytics.vo.ReportPageVO;
import com.youthhealth.modules.analytics.vo.ReportVO;
import com.youthhealth.modules.analytics.vo.SportTrendPointVO;
import com.youthhealth.modules.diet.mapper.DietRecordMapper;
import com.youthhealth.modules.diet.vo.DietSummaryRow;
import com.youthhealth.modules.sport.mapper.SportRecordMapper;
import com.youthhealth.modules.sport.vo.SportSummaryRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;
    private final AdviceMapper adviceMapper;
    private final DietRecordMapper dietRecordMapper;
    private final SportRecordMapper sportRecordMapper;
    private final StatsMapper statsMapper;
    private final ObjectMapper objectMapper;

    @Override
    public ReportVO generateWeekly(Long userId, WeeklyReportGenerateRequest request) {
        LocalDate base = request == null || request.getWeekStartDate() == null
                ? LocalDate.now().with(DayOfWeek.MONDAY)
                : request.getWeekStartDate();
        LocalDate weekStart = base.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        ReportPayload payload = buildPayload(userId, weekStart, weekEnd);
        WeeklyReport weeklyReport = new WeeklyReport();
        weeklyReport.setUserId(userId);
        weeklyReport.setWeekStartDate(weekStart);
        weeklyReport.setWeekEndDate(weekEnd);
        weeklyReport.setSummaryJson(payload.summaryJson());
        weeklyReport.setReportText(payload.reportText());
        reportMapper.insertWeekly(weeklyReport);

        return ReportVO.builder()
                .id(weeklyReport.getId())
                .reportType("WEEKLY")
                .periodStart(weekStart)
                .periodEnd(weekEnd)
                .title("Weekly Report " + weekStart + " ~ " + weekEnd)
                .summaryJson(payload.summaryJson())
                .reportText(payload.reportText())
                .createdAt(weeklyReport.getCreatedAt())
                .build();
    }

    @Override
    public ReportVO generateMonthly(Long userId, MonthlyReportGenerateRequest request) {
        YearMonth month = parseMonth(request == null ? null : request.getReportMonth());
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        ReportPayload payload = buildPayload(userId, start, end);
        MonthlyReport monthlyReport = new MonthlyReport();
        monthlyReport.setUserId(userId);
        monthlyReport.setReportMonth(month.toString());
        monthlyReport.setMonthStartDate(start);
        monthlyReport.setMonthEndDate(end);
        monthlyReport.setSummaryJson(payload.summaryJson());
        monthlyReport.setReportText(payload.reportText());
        reportMapper.insertMonthly(monthlyReport);

        return ReportVO.builder()
                .id(monthlyReport.getId())
                .reportType("MONTHLY")
                .periodStart(start)
                .periodEnd(end)
                .title("Monthly Report " + month)
                .summaryJson(payload.summaryJson())
                .reportText(payload.reportText())
                .createdAt(monthlyReport.getCreatedAt())
                .build();
    }

    @Override
    public ReportPageVO page(Long userId, Integer page, Integer size, String reportType) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        String type = normalizeType(reportType);
        int offset = (safePage - 1) * safeSize;
        Long total = reportMapper.countPage(userId, type);
        List<ReportVO> records = (total == null || total == 0)
                ? List.of()
                : reportMapper.page(userId, type, offset, safeSize);
        return ReportPageVO.builder()
                .total(total == null ? 0L : total)
                .page(safePage)
                .size(safeSize)
                .records(records)
                .build();
    }

    private ReportPayload buildPayload(Long userId, LocalDate start, LocalDate end) {
        DietSummaryRow diet = dietRecordMapper.summaryByDateRange(userId, start, end);
        SportSummaryRow sport = sportRecordMapper.summaryByDateRange(userId, start, end);
        List<DietTrendPointVO> dietTrend = statsMapper.dietTrendByDateRange(userId, start, end);
        List<SportTrendPointVO> sportTrend = statsMapper.sportTrendByDateRange(userId, start, end);

        BigDecimal intakeCalories = safe(diet == null ? null : diet.getTotalCalories());
        BigDecimal burnCalories = safe(sport == null ? null : sport.getTotalCaloriesBurned());
        BigDecimal energyGap = intakeCalories.subtract(burnCalories).setScale(2, RoundingMode.HALF_UP);
        AdviceVO latestAdvice = toAdviceVO(userId);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("periodStart", start);
        summary.put("periodEnd", end);
        summary.put("intakeCalories", intakeCalories);
        summary.put("burnCalories", burnCalories);
        summary.put("energyGap", energyGap);
        summary.put("protein", safe(diet == null ? null : diet.getTotalProtein()));
        summary.put("fat", safe(diet == null ? null : diet.getTotalFat()));
        summary.put("carb", safe(diet == null ? null : diet.getTotalCarb()));
        summary.put("steps", sport == null || sport.getTotalSteps() == null ? 0 : sport.getTotalSteps());
        summary.put("durationMin", sport == null || sport.getTotalDurationMin() == null ? 0 : sport.getTotalDurationMin());
        summary.put("sportTrend", sportTrendForChart(sportTrend));
        summary.put("dietTrend", dietTrendForChart(dietTrend));
        summary.put("latestAdviceText", latestAdvice == null ? null : latestAdvice.getAdviceText());

        String summaryJson = toJson(summary);
        String reportText = "Report Period: " + start + " to " + end + "\n"
                + "Energy Intake: " + intakeCalories + " kcal\n"
                + "Energy Burned: " + burnCalories + " kcal\n"
                + "Energy Gap: " + energyGap + " kcal\n"
                + "Protein/Fat/Carb(g): " + safe(diet == null ? null : diet.getTotalProtein()) + "/"
                + safe(diet == null ? null : diet.getTotalFat()) + "/"
                + safe(diet == null ? null : diet.getTotalCarb()) + "\n"
                + "Total Steps: " + (sport == null || sport.getTotalSteps() == null ? 0 : sport.getTotalSteps()) + "\n"
                + "Total Exercise Duration: " + (sport == null || sport.getTotalDurationMin() == null ? 0 : sport.getTotalDurationMin()) + " min"
                + (latestAdvice == null ? "" : "\nAdvice: " + latestAdvice.getAdviceText());
        return new ReportPayload(summaryJson, reportText);
    }

    private List<Map<String, Object>> dietTrendForChart(List<DietTrendPointVO> rows) {
        return rows.stream().map(row -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("date", row.getDate());
            m.put("caloriesIntake", safe(row.getCaloriesIntake()));
            m.put("protein", safe(row.getProtein()));
            m.put("fat", safe(row.getFat()));
            m.put("carb", safe(row.getCarb()));
            return m;
        }).toList();
    }

    private List<Map<String, Object>> sportTrendForChart(List<SportTrendPointVO> rows) {
        return rows.stream().map(row -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("date", row.getDate());
            m.put("caloriesBurned", safe(row.getCaloriesBurned()));
            m.put("steps", row.getSteps() == null ? 0 : row.getSteps());
            m.put("durationMin", row.getDurationMin() == null ? 0 : row.getDurationMin());
            return m;
        }).toList();
    }

    private AdviceVO toAdviceVO(Long userId) {
        var latest = adviceMapper.findLatestByUserId(userId);
        if (latest == null) {
            return null;
        }
        return AdviceVO.builder()
                .id(latest.getId())
                .energyAdvice(latest.getEnergyAdvice())
                .dietAdvice(latest.getDietAdvice())
                .sportAdvice(latest.getSportAdvice())
                .adviceText(latest.getAdviceText())
                .summaryJson(latest.getSummaryJson())
                .sourceType(latest.getSourceType())
                .createdAt(latest.getCreatedAt())
                .build();
    }

    private String normalizeType(String reportType) {
        if (!StringUtils.hasText(reportType)) {
            return null;
        }
        String type = reportType.trim().toUpperCase(Locale.ROOT);
        if (!"WEEKLY".equals(type) && !"MONTHLY".equals(type)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "reportType must be WEEKLY or MONTHLY");
        }
        return type;
    }

    private YearMonth parseMonth(String reportMonth) {
        if (!StringUtils.hasText(reportMonth)) {
            return YearMonth.now();
        }
        try {
            return YearMonth.parse(reportMonth.trim());
        } catch (DateTimeParseException ex) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "reportMonth must be yyyy-MM");
        }
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

    private record ReportPayload(String summaryJson, String reportText) {
    }
}
