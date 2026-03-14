package com.youthhealth.modules.diet.controller;

import com.youthhealth.common.api.Result;
import com.youthhealth.modules.diet.entity.FoodItem;
import com.youthhealth.modules.diet.service.FoodService;
import com.youthhealth.modules.diet.vo.FoodPageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/page")
    public Result<FoodPageVO> page(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String keyword) {
        return Result.success(foodService.page(page, size, keyword));
    }

    @GetMapping("/search")
    public Result<List<FoodItem>> search(@RequestParam String keyword) {
        return Result.success(foodService.search(keyword));
    }
}
