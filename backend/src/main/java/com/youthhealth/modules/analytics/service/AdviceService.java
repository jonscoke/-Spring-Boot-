package com.youthhealth.modules.analytics.service;

import com.youthhealth.modules.analytics.vo.AdviceVO;

public interface AdviceService {
    AdviceVO latest(Long userId);

    AdviceVO generate(Long userId);
}
