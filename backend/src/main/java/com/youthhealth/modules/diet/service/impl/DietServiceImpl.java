package com.youthhealth.modules.diet.service.impl;

import com.youthhealth.common.enums.ResultCode;
import com.youthhealth.common.exception.BusinessException;
import com.youthhealth.modules.diet.dto.DietCreateRequest;
import com.youthhealth.modules.diet.dto.DietRecordItemRequest;
import com.youthhealth.modules.diet.entity.DietRecord;
import com.youthhealth.modules.diet.entity.DietRecordItem;
import com.youthhealth.modules.diet.entity.FoodItem;
import com.youthhealth.modules.diet.mapper.DietRecordMapper;
import com.youthhealth.modules.diet.mapper.FoodItemMapper;
import com.youthhealth.modules.diet.service.DietService;
import com.youthhealth.modules.diet.vo.DietPageVO;
import com.youthhealth.modules.diet.vo.DietRecordVO;
import com.youthhealth.modules.diet.vo.DietSummaryRow;
import com.youthhealth.modules.diet.vo.DietSummaryVO;
import com.youthhealth.modules.diet.vo.NutritionRatioVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {

    private final DietRecordMapper dietRecordMapper;
    private final FoodItemMapper foodItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DietRecordVO create(Long userId, DietCreateRequest request) {
        List<Long> foodIds = request.getItems().stream()
                .map(DietRecordItemRequest::getFoodItemId)
                .distinct()
                .toList();
        List<FoodItem> foods = foodItemMapper.findByIds(foodIds);
        Map<Long, FoodItem> foodMap = foods.stream().collect(Collectors.toMap(FoodItem::getId, f -> f));
        if (foodMap.size() != foodIds.size()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "food item not found");
        }

        DietRecord record = new DietRecord();
        record.setUserId(userId);
        record.setMealType(normalizeMealType(request.getMealType()));
        record.setRecordDate(request.getRecordDate());
        record.setRemark(request.getRemark());

        List<DietRecordItem> items = new ArrayList<>();
        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalFat = BigDecimal.ZERO;
        BigDecimal totalCarb = BigDecimal.ZERO;

        for (DietRecordItemRequest reqItem : request.getItems()) {
            FoodItem food = foodMap.get(reqItem.getFoodItemId());
            DietRecordItem item = new DietRecordItem();
            item.setFoodItemId(food.getId());
            item.setFoodName(food.getFoodName());
            item.setIntakeGram(scale(reqItem.getIntakeGram()));

            BigDecimal ratio = reqItem.getIntakeGram().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            item.setCalories(scale(food.getCaloriePer100g().multiply(ratio)));
            item.setProtein(scale(food.getProteinPer100g().multiply(ratio)));
            item.setFat(scale(food.getFatPer100g().multiply(ratio)));
            item.setCarb(scale(food.getCarbPer100g().multiply(ratio)));

            totalCalories = totalCalories.add(item.getCalories());
            totalProtein = totalProtein.add(item.getProtein());
            totalFat = totalFat.add(item.getFat());
            totalCarb = totalCarb.add(item.getCarb());
            items.add(item);
        }

        record.setTotalCalories(scale(totalCalories));
        record.setTotalProtein(scale(totalProtein));
        record.setTotalFat(scale(totalFat));
        record.setTotalCarb(scale(totalCarb));
        dietRecordMapper.insertRecord(record);

        for (DietRecordItem item : items) {
            item.setDietRecordId(record.getId());
        }
        dietRecordMapper.batchInsertItems(items);
        return DietRecordVO.builder().record(record).items(items).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId, Long id) {
        int deleted = dietRecordMapper.deleteRecordByIdAndUserId(id, userId);
        if (deleted == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
    }

    @Override
    public DietPageVO page(Long userId, Integer page, Integer size, LocalDate startDate, LocalDate endDate) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        int offset = (safePage - 1) * safeSize;
        Long total = dietRecordMapper.countByUserId(userId, startDate, endDate);
        if (total == null || total == 0) {
            return DietPageVO.builder()
                    .total(0L)
                    .page(safePage)
                    .size(safeSize)
                    .records(List.of())
                    .build();
        }

        List<DietRecord> records = dietRecordMapper.pageByUserId(userId, startDate, endDate, offset, safeSize);
        List<Long> recordIds = records.stream().map(DietRecord::getId).toList();
        List<DietRecordItem> allItems = dietRecordMapper.listItemsByRecordIds(recordIds);
        Map<Long, List<DietRecordItem>> itemMap = new HashMap<>();
        for (DietRecordItem item : allItems) {
            itemMap.computeIfAbsent(item.getDietRecordId(), k -> new ArrayList<>()).add(item);
        }

        List<DietRecordVO> result = records.stream()
                .map(record -> DietRecordVO.builder()
                        .record(record)
                        .items(itemMap.getOrDefault(record.getId(), List.of()))
                        .build())
                .toList();

        return DietPageVO.builder()
                .total(total)
                .page(safePage)
                .size(safeSize)
                .records(result)
                .build();
    }

    @Override
    public DietSummaryVO dailySummary(Long userId, LocalDate date) {
        LocalDate target = date == null ? LocalDate.now() : date;
        return buildSummary(userId, target, target);
    }

    @Override
    public DietSummaryVO weeklySummary(Long userId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        return buildSummary(userId, start, end);
    }

    @Override
    public NutritionRatioVO nutritionRatio(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDate end = endDate == null ? LocalDate.now() : endDate;
        LocalDate start = startDate == null ? end.minusDays(6) : startDate;
        DietSummaryRow row = dietRecordMapper.summaryByDateRange(userId, start, end);

        BigDecimal proteinCalories = safe(row == null ? null : row.getTotalProtein()).multiply(BigDecimal.valueOf(4));
        BigDecimal fatCalories = safe(row == null ? null : row.getTotalFat()).multiply(BigDecimal.valueOf(9));
        BigDecimal carbCalories = safe(row == null ? null : row.getTotalCarb()).multiply(BigDecimal.valueOf(4));
        BigDecimal total = proteinCalories.add(fatCalories).add(carbCalories);
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            return NutritionRatioVO.builder()
                    .startDate(start)
                    .endDate(end)
                    .proteinCalorieRatio(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                    .fatCalorieRatio(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                    .carbCalorieRatio(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                    .build();
        }
        return NutritionRatioVO.builder()
                .startDate(start)
                .endDate(end)
                .proteinCalorieRatio(proteinCalories.divide(total, 4, RoundingMode.HALF_UP))
                .fatCalorieRatio(fatCalories.divide(total, 4, RoundingMode.HALF_UP))
                .carbCalorieRatio(carbCalories.divide(total, 4, RoundingMode.HALF_UP))
                .build();
    }

    private DietSummaryVO buildSummary(Long userId, LocalDate start, LocalDate end) {
        DietSummaryRow row = dietRecordMapper.summaryByDateRange(userId, start, end);
        return DietSummaryVO.builder()
                .startDate(start)
                .endDate(end)
                .recordCount(row == null || row.getRecordCount() == null ? 0L : row.getRecordCount())
                .totalCalories(safe(row == null ? null : row.getTotalCalories()))
                .totalProtein(safe(row == null ? null : row.getTotalProtein()))
                .totalFat(safe(row == null ? null : row.getTotalFat()))
                .totalCarb(safe(row == null ? null : row.getTotalCarb()))
                .build();
    }

    private String normalizeMealType(String mealType) {
        if (!StringUtils.hasText(mealType)) {
            return "OTHER";
        }
        return mealType.trim().toUpperCase(Locale.ROOT);
    }

    private BigDecimal scale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal safe(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
