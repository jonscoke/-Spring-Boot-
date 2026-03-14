package com.youthhealth.modules.social.service.impl;

import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.social.dto.GoalCreateRequest;
import com.youthhealth.modules.social.dto.GoalUpdateRequest;
import com.youthhealth.modules.social.entity.UserGoal;
import com.youthhealth.modules.social.mapper.GoalMapper;
import com.youthhealth.modules.social.service.GoalService;
import com.youthhealth.modules.social.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalMapper goalMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserGoal create(Long userId, GoalCreateRequest request) {
        UserGoal goal = toGoal(userId, request);
        goalMapper.insert(goal);
        return goal;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserGoal update(Long userId, Long id, GoalUpdateRequest request) {
        UserGoal exists = goalMapper.findByIdAndUserId(id, userId);
        if (exists == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        UserGoal goal = toGoal(userId, request);
        goal.setId(id);
        if (goalMapper.updateByIdAndUserId(goal) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return goalMapper.findByIdAndUserId(id, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long id) {
        if (goalMapper.deleteByIdAndUserId(id, userId) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }

    @Override
    public PageVO<UserGoal> page(Long userId, Integer page, Integer size) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        int offset = (safePage - 1) * safeSize;
        Long total = goalMapper.countByUserId(userId);
        List<UserGoal> records = total == null || total == 0 ? List.of() : goalMapper.pageByUserId(userId, offset, safeSize);
        return PageVO.<UserGoal>builder()
                .total(total == null ? 0L : total)
                .page(safePage)
                .size(safeSize)
                .records(records)
                .build();
    }

    private UserGoal toGoal(Long userId, GoalCreateRequest request) {
        UserGoal goal = new UserGoal();
        goal.setUserId(userId);
        goal.setGoalType(request.getGoalType().trim().toUpperCase(Locale.ROOT));
        goal.setGoalValue(request.getGoalValue());
        goal.setStartDate(request.getStartDate());
        goal.setEndDate(request.getEndDate());
        goal.setStatus(StringUtils.hasText(request.getStatus()) ? request.getStatus().trim().toUpperCase(Locale.ROOT) : "ACTIVE");
        return goal;
    }

    private UserGoal toGoal(Long userId, GoalUpdateRequest request) {
        GoalCreateRequest createRequest = new GoalCreateRequest();
        createRequest.setGoalType(request.getGoalType());
        createRequest.setGoalValue(request.getGoalValue());
        createRequest.setStartDate(request.getStartDate());
        createRequest.setEndDate(request.getEndDate());
        createRequest.setStatus(request.getStatus());
        return toGoal(userId, createRequest);
    }
}
