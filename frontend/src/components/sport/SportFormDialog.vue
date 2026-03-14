<template>
  <el-dialog :model-value="visible" :title="mode === 'create' ? '新增运动记录' : '编辑运动记录'" width="560px" @close="emit('close')">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="运动类型" prop="sportType">
            <el-select v-model="form.sportType">
              <el-option label="跑步" value="RUN" />
              <el-option label="步行" value="WALK" />
              <el-option label="骑行" value="CYCLE" />
              <el-option label="游泳" value="SWIM" />
              <el-option label="球类" value="BALL" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="记录日期" prop="recordDate">
            <el-date-picker v-model="form.recordDate" type="date" value-format="YYYY-MM-DD" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="步数" prop="steps">
            <el-input-number v-model="form.steps" :min="0" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="距离(km)" prop="distanceKm">
            <el-input-number v-model="form.distanceKm" :min="0" :step="0.1" :precision="2" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="时长(min)" prop="durationMin">
            <el-input-number v-model="form.durationMin" :min="0" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="配速" prop="pace">
            <el-input-number v-model="form.pace" :min="0" :step="0.1" :precision="2" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="消耗热量" prop="caloriesBurned">
            <el-input-number v-model="form.caloriesBurned" :min="0" :step="1" :precision="2" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="来源" prop="sourceType">
            <el-select v-model="form.sourceType">
              <el-option label="手动录入" value="MANUAL" />
              <el-option label="模拟同步" value="MOCK_APP" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <el-button @click="emit('close')">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { SportRecord } from '@/types/api'

const props = defineProps<{
  visible: boolean
  mode: 'create' | 'edit'
  initialValue?: SportRecord | null
  submitting?: boolean
}>()

const emit = defineEmits<{
  close: []
  submit: [payload: SportRecord]
}>()

const formRef = ref<FormInstance>()
const form = reactive<SportRecord>({
  sportType: 'RUN',
  steps: 0,
  distanceKm: 0,
  durationMin: 0,
  pace: 0,
  caloriesBurned: 0,
  sourceType: 'MANUAL',
  externalId: null,
  recordDate: '',
})

const rules: FormRules<typeof form> = {
  sportType: [{ required: true, message: '请选择运动类型', trigger: 'change' }],
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  steps: [{ required: true, message: '请输入步数', trigger: 'blur' }],
  distanceKm: [{ required: true, message: '请输入距离', trigger: 'blur' }],
  durationMin: [{ required: true, message: '请输入时长', trigger: 'blur' }],
  sourceType: [{ required: true, message: '请选择来源', trigger: 'change' }],
}

watch(
  () => props.initialValue,
  (value) => {
    form.sportType = value?.sportType || 'RUN'
    form.steps = Number(value?.steps || 0)
    form.distanceKm = Number(value?.distanceKm || 0)
    form.durationMin = Number(value?.durationMin || 0)
    form.pace = Number(value?.pace || 0)
    form.caloriesBurned = Number(value?.caloriesBurned || 0)
    form.sourceType = value?.sourceType || 'MANUAL'
    form.externalId = value?.externalId || null
    form.recordDate = value?.recordDate || ''
  },
  { immediate: true },
)

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  emit('submit', { ...form })
}
</script>
