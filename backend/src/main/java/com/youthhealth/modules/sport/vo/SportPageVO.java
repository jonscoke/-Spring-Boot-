package com.youthhealth.modules.sport.vo;

import com.youthhealth.modules.sport.entity.SportRecord;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SportPageVO {
    private Long total;
    private Integer page;
    private Integer size;
    private List<SportRecord> records;
}
