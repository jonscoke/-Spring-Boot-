package com.youthhealth.modules.diet.mapper;

import com.youthhealth.modules.diet.entity.FoodItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodItemMapper {
    Long countByKeyword(@Param("keyword") String keyword);

    List<FoodItem> pageByKeyword(@Param("keyword") String keyword,
                                 @Param("offset") Integer offset,
                                 @Param("size") Integer size);

    List<FoodItem> searchByName(@Param("keyword") String keyword,
                                @Param("limitSize") Integer limitSize);

    List<FoodItem> findByIds(@Param("ids") List<Long> ids);
}
