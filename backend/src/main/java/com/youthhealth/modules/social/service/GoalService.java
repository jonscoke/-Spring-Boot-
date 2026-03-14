package com.youthhealth.modules.social.service;

import com.youthhealth.modules.social.dto.GoalCreateRequest;
import com.youthhealth.modules.social.dto.GoalUpdateRequest;
import com.youthhealth.modules.social.entity.UserGoal;
import com.youthhealth.modules.social.vo.PageVO;

public interface GoalService {
    UserGoal create(Long userId, GoalCreateRequest request);

    UserGoal update(Long userId, Long id, GoalUpdateRequest request);

    void delete(Long userId, Long id);

    PageVO<UserGoal> page(Long userId, Integer page, Integer size);
}
