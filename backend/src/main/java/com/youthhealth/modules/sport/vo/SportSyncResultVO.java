package com.youthhealth.modules.sport.vo;

import com.youthhealth.modules.sport.entity.SportRecord;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SportSyncResultVO {
    private String sourceType;
    private Integer totalMockCount;
    private Integer insertedCount;
    private Integer duplicateCount;
    private List<SportRecord> mockSamples;
}
