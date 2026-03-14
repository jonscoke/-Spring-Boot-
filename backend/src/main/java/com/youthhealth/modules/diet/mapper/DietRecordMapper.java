package com.youthhealth.modules.diet.mapper;

import com.youthhealth.modules.diet.entity.DietRecord;
import com.youthhealth.modules.diet.entity.DietRecordItem;
import com.youthhealth.modules.diet.vo.DietSummaryRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DietRecordMapper {
    int insertRecord(DietRecord record);

    int batchInsertItems(@Param("items") List<DietRecordItem> items);

    int deleteRecordByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Long countByUserId(@Param("userId") Long userId,
                       @Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate);

    List<DietRecord> pageByUserId(@Param("userId") Long userId,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  @Param("offset") Integer offset,
                                  @Param("size") Integer size);

    List<DietRecordItem> listItemsByRecordIds(@Param("recordIds") List<Long> recordIds);

    DietSummaryRow summaryByDateRange(@Param("userId") Long userId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}
