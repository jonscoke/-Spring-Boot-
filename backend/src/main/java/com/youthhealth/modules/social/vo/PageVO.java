package com.youthhealth.modules.social.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageVO<T> {
    private Long total;
    private Integer page;
    private Integer size;
    private List<T> records;
}
