package com.youthhealth.modules.social.service.impl;

import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.social.dto.CheckinCreateRequest;
import com.youthhealth.modules.social.entity.UserCheckin;
import com.youthhealth.modules.social.mapper.CheckinMapper;
import com.youthhealth.modules.social.mapper.GoalMapper;
import com.youthhealth.modules.social.service.CheckinService;
import com.youthhealth.modules.social.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckinServiceImpl implements CheckinService {

    private final CheckinMapper checkinMapper;
    private final GoalMapper goalMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCheckin create(Long userId, CheckinCreateRequest request) {
        if (checkinMapper.findByUserIdAndDate(userId, request.getCheckinDate()) != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "checkin already exists for this date");
        }
        if (request.getGoalId() != null && goalMapper.findByIdAndUserId(request.getGoalId(), userId) == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "goal not found");
        }
        UserCheckin checkin = new UserCheckin();
        checkin.setUserId(userId);
        checkin.setCheckinDate(request.getCheckinDate());
        checkin.setContent(request.getContent().trim());
        checkin.setMoodScore(request.getMoodScore());
        checkin.setGoalId(request.getGoalId());
        checkinMapper.insert(checkin);
        return checkin;
    }

    @Override
    public PageVO<UserCheckin> page(Long userId, Integer page, Integer size) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        int offset = (safePage - 1) * safeSize;
        Long total = checkinMapper.countByUserId(userId);
        List<UserCheckin> records = total == null || total == 0 ? List.of() : checkinMapper.pageByUserId(userId, offset, safeSize);
        return PageVO.<UserCheckin>builder()
                .total(total == null ? 0L : total)
                .page(safePage)
                .size(safeSize)
                .records(records)
                .build();
    }
}
