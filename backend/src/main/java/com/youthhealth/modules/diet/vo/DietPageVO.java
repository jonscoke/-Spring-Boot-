package com.youthhealth.modules.diet.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DietPageVO {
    private Long total;
    private Integer page;
    private Integer size;
    private List<DietRecordVO> records;
}
