package com.youthhealth.modules.diet.service;

import com.youthhealth.modules.diet.entity.FoodItem;
import com.youthhealth.modules.diet.vo.FoodPageVO;

import java.util.List;

public interface FoodService {
    FoodPageVO page(Integer page, Integer size, String keyword);

    List<FoodItem> search(String keyword);
}
