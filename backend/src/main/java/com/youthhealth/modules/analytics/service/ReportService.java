package com.youthhealth.modules.analytics.service;

import com.youthhealth.modules.analytics.dto.MonthlyReportGenerateRequest;
import com.youthhealth.modules.analytics.dto.WeeklyReportGenerateRequest;
import com.youthhealth.modules.analytics.vo.ReportPageVO;
import com.youthhealth.modules.analytics.vo.ReportVO;

public interface ReportService {
    ReportVO generateWeekly(Long userId, WeeklyReportGenerateRequest request);

    ReportVO generateMonthly(Long userId, MonthlyReportGenerateRequest request);

    ReportPageVO page(Long userId, Integer page, Integer size, String reportType);
}
