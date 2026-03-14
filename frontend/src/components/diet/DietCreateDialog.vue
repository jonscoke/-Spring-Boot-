<template>
  <el-dialog :model-value="visible" title="新增饮食记录" width="760px" @close="emit('close')">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="餐别" prop="mealType">
            <el-select v-model="form.mealType">
              <el-option label="早餐" value="BREAKFAST" />
              <el-option label="午餐" value="LUNCH" />
              <el-option label="晚餐" value="DINNER" />
              <el-option label="加餐" value="SNACK" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="日期" prop="recordDate">
            <el-date-picker v-model="form.recordDate" type="date" value-format="YYYY-MM-DD" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" />
      </el-form-item>
    </el-form>

    <FoodSearchList @select="addFood" />

    <div class="selected-foods">
      <div class="panel__head">
        <h3>已选食物</h3>
      </div>
      <EmptyBlock v-if="!items.length" description="请先搜索并添加食物" />
      <el-table v-else :data="items" size="small">
        <el-table-column prop="foodName" label="食物" />
        <el-table-column label="摄入(g)" width="140">
          <template #default="{ row }">
            <el-input-number v-model="row.intakeGram" :min="1" :step="10" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ $index }">
            <el-button link type="danger" @click="removeFood($index)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <template #footer>
      <el-button @click="emit('close')">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import FoodSearchList from '@/components/diet/FoodSearchList.vue'
import type { DietRecordItemPayload, FoodItem } from '@/types/api'

const props = defineProps<{
  visible: boolean
  submitting?: boolean
}>()

const emit = defineEmits<{
  close: []
  submit: [payload: { mealType: string; recordDate: string; remark?: string; items: DietRecordItemPayload[] }]
}>()

const formRef = ref<FormInstance>()
const form = reactive({
  mealType: 'LUNCH',
  recordDate: new Date().toISOString().slice(0, 10),
  remark: '',
})
const items = ref<Array<DietRecordItemPayload & { foodName: string }>>([])

const rules: FormRules<typeof form> = {
  mealType: [{ required: true, message: '请选择餐别', trigger: 'change' }],
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
}

function addFood(food: FoodItem) {
  const exists = items.value.find((item) => item.foodItemId === food.id)
  if (exists) {
    exists.intakeGram += 50
    return
  }
  items.value.push({
    foodItemId: food.id,
    intakeGram: 100,
    foodName: food.foodName,
  })
}

function removeFood(index: number) {
  items.value.splice(index, 1)
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid || !items.value.length) {
    return
  }
  emit('submit', {
    mealType: form.mealType,
    recordDate: form.recordDate,
    remark: form.remark || undefined,
    items: items.value.map((item) => ({
      foodItemId: item.foodItemId,
      intakeGram: item.intakeGram,
    })),
  })
}
</script>
