package com.youthhealth.modules.diet.service.impl;

import com.youthhealth.modules.diet.entity.FoodItem;
import com.youthhealth.modules.diet.mapper.FoodItemMapper;
import com.youthhealth.modules.diet.service.FoodService;
import com.youthhealth.modules.diet.vo.FoodPageVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private static final Duration COMMON_FOOD_CACHE_TTL = Duration.ofMinutes(30);
    private static final Duration SEARCH_FOOD_CACHE_TTL = Duration.ofMinutes(10);
    private static final int SEARCH_LIMIT = 20;

    private final FoodItemMapper foodItemMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public FoodPageVO page(Integer page, Integer size, String keyword) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 10 : Math.min(size, 100);
        String normalizedKeyword = normalizeKeyword(keyword);
        boolean useCache = !StringUtils.hasText(normalizedKeyword) && safePage == 1;
        String cacheKey = "food:common:page:" + safePage + ":" + safeSize;
        if (useCache) {
            FoodPageVO cached = getCache(cacheKey, FoodPageVO.class);
            if (cached != null) {
                return cached;
            }
        }

        int offset = (safePage - 1) * safeSize;
        Long total = foodItemMapper.countByKeyword(normalizedKeyword);
        List<FoodItem> records = (total == null || total == 0)
                ? List.of()
                : foodItemMapper.pageByKeyword(normalizedKeyword, offset, safeSize);

        FoodPageVO result = FoodPageVO.builder()
                .total(total == null ? 0L : total)
                .page(safePage)
                .size(safeSize)
                .records(records)
                .build();

        if (useCache) {
            setCache(cacheKey, result, COMMON_FOOD_CACHE_TTL);
        }
        return result;
    }

    @Override
    public List<FoodItem> search(String keyword) {
        String normalizedKeyword = normalizeKeyword(keyword);
        if (!StringUtils.hasText(normalizedKeyword)) {
            return List.of();
        }
        String cacheKey = "food:hot:search:" + normalizedKeyword.toLowerCase();
        List<FoodItem> cached = getListCache(cacheKey);
        if (cached != null) {
            return cached;
        }
        List<FoodItem> records = foodItemMapper.searchByName(normalizedKeyword, SEARCH_LIMIT);
        setCache(cacheKey, records, SEARCH_FOOD_CACHE_TTL);
        return records;
    }

    private String normalizeKeyword(String keyword) {
        return StringUtils.hasText(keyword) ? keyword.trim() : null;
    }

    private <T> T getCache(String key, Class<T> clazz) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj instanceof String value) {
                return objectMapper.readValue(value, clazz);
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private List<FoodItem> getListCache(String key) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj instanceof String value) {
                return objectMapper.readValue(value, new TypeReference<List<FoodItem>>() {
                });
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private void setCache(String key, Object value, Duration ttl) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttl);
        } catch (Exception ignored) {
        }
    }
}
