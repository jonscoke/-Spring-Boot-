<template>
  <section class="panel">
    <div class="panel__head">
      <h3>目标设定</h3>
      <el-button type="primary" @click="openCreate">新增目标</el-button>
    </div>

    <EmptyBlock v-if="!goals.length" description="暂无目标" />
    <el-table v-else :data="goals" size="small">
      <el-table-column prop="goalType" label="类型" />
      <el-table-column prop="goalValue" label="目标值" />
      <el-table-column prop="status" label="状态" />
      <el-table-column prop="startDate" label="开始" />
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="emit('delete', row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :model-value="dialogVisible" :title="editingId ? '编辑目标' : '新增目标'" width="520px" @close="dialogVisible = false">
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="目标类型" prop="goalType">
          <el-input v-model="form.goalType" />
        </el-form-item>
        <el-form-item label="目标值" prop="goalValue">
          <el-input-number v-model="form.goalValue" :min="0.01" :precision="2" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import type { GoalRecord } from '@/types/api'

const props = defineProps<{
  goals: GoalRecord[]
}>()

const emit = defineEmits<{
  create: [payload: Omit<GoalRecord, 'id'>]
  update: [id: number, payload: Omit<GoalRecord, 'id'>]
  delete: [id: number]
}>()

const formRef = ref<FormInstance>()
const dialogVisible = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<Omit<GoalRecord, 'id'>>({
  goalType: '',
  goalValue: 1,
  startDate: new Date().toISOString().slice(0, 10),
  endDate: '',
  status: 'ACTIVE',
})

const rules: FormRules<typeof form> = {
  goalType: [{ required: true, message: '请输入目标类型', trigger: 'blur' }],
  goalValue: [{ required: true, message: '请输入目标值', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
}

function openCreate() {
  editingId.value = null
  form.goalType = ''
  form.goalValue = 1
  form.startDate = new Date().toISOString().slice(0, 10)
  form.endDate = ''
  form.status = 'ACTIVE'
  dialogVisible.value = true
}

function openEdit(row: GoalRecord) {
  editingId.value = row.id
  form.goalType = row.goalType
  form.goalValue = row.goalValue
  form.startDate = row.startDate
  form.endDate = row.endDate || ''
  form.status = row.status
  dialogVisible.value = true
}

async function submit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  const payload = { ...form, endDate: form.endDate || undefined }
  if (editingId.value) {
    emit('update', editingId.value, payload)
  } else {
    emit('create', payload)
  }
  dialogVisible.value = false
}
</script>
