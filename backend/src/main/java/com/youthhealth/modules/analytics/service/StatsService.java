package com.youthhealth.modules.analytics.service;

import com.youthhealth.modules.analytics.vo.StatsDashboardVO;

public interface StatsService {
    StatsDashboardVO dashboard(Long userId);
}
