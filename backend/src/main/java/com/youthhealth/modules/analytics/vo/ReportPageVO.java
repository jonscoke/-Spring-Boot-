package com.youthhealth.modules.analytics.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportPageVO {
    private Long total;
    private Integer page;
    private Integer size;
    private List<ReportVO> records;
}
