<template>
  <section class="panel">
    <div class="panel__head">
      <h3>每日打卡</h3>
    </div>

    <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
      <el-form-item label="打卡日期" prop="checkinDate">
        <el-date-picker v-model="form.checkinDate" type="date" value-format="YYYY-MM-DD" />
      </el-form-item>
      <el-form-item label="打卡内容" prop="content">
        <el-input v-model="form.content" type="textarea" :rows="3" />
      </el-form-item>
      <el-form-item label="心情评分">
        <el-rate v-model="form.moodScore" :max="5" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">提交打卡</el-button>
      </el-form-item>
    </el-form>

    <EmptyBlock v-if="!checkins.length" description="暂无打卡记录" />
    <div v-else class="stack-list">
      <article v-for="item in checkins" :key="item.id" class="stack-card">
        <strong>{{ item.checkinDate }}</strong>
        <p>{{ item.content }}</p>
      </article>
    </div>
  </section>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import type { CheckinRecord } from '@/types/api'

defineProps<{
  checkins: CheckinRecord[]
}>()

const emit = defineEmits<{
  submit: [payload: Omit<CheckinRecord, 'id'>]
}>()

const formRef = ref<FormInstance>()
const form = reactive<Omit<CheckinRecord, 'id'>>({
  checkinDate: new Date().toISOString().slice(0, 10),
  content: '',
  moodScore: 5,
  goalId: null,
})

const rules: FormRules<typeof form> = {
  checkinDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
}

async function submit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  emit('submit', { ...form })
  form.content = ''
}
</script>
