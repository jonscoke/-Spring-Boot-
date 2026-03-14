package com.youthhealth.modules.diet.vo;

import com.youthhealth.modules.diet.entity.DietRecord;
import com.youthhealth.modules.diet.entity.DietRecordItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DietRecordVO {
    private DietRecord record;
    private List<DietRecordItem> items;
}
