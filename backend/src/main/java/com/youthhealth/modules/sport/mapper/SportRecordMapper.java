package com.youthhealth.modules.sport.mapper;

import com.youthhealth.modules.sport.entity.SportRecord;
import com.youthhealth.modules.sport.entity.SportSyncLog;
import com.youthhealth.modules.sport.vo.SportSummaryRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SportRecordMapper {
    int insert(SportRecord record);

    int updateByIdAndUserId(SportRecord record);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    SportRecord findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Long countByUserId(@Param("userId") Long userId,
                       @Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate);

    List<SportRecord> pageByUserId(@Param("userId") Long userId,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("offset") Integer offset,
                                   @Param("size") Integer size);

    List<SportRecord> listByRecordDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);

    SportSummaryRow summaryByDateRange(@Param("userId") Long userId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    SportRecord findByUniqueSource(@Param("userId") Long userId,
                                   @Param("sourceType") String sourceType,
                                   @Param("externalId") String externalId);

    int insertSyncLog(SportSyncLog syncLog);
}
