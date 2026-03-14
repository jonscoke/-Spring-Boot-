package com.youthhealth.modules.diet.vo;

import com.youthhealth.modules.diet.entity.FoodItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FoodPageVO {
    private Long total;
    private Integer page;
    private Integer size;
    private List<FoodItem> records;
}
