package com.youthhealth.modules.social.service;

import com.youthhealth.modules.social.dto.CheckinCreateRequest;
import com.youthhealth.modules.social.entity.UserCheckin;
import com.youthhealth.modules.social.vo.PageVO;

public interface CheckinService {
    UserCheckin create(Long userId, CheckinCreateRequest request);

    PageVO<UserCheckin> page(Long userId, Integer page, Integer size);
}
