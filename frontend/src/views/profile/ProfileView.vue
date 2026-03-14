<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>个人档案</h3>
        <el-button type="primary" @click="editing = !editing">{{ editing ? '取消编辑' : '编辑档案' }}</el-button>
      </div>

      <el-row :gutter="20">
        <el-col :md="12" :span="24">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="昵称">{{ userStore.profile?.nickname || '-' }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ userStore.profile?.username || '-' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ userStore.profile?.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userStore.profile?.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ userStore.profile?.roles.join(', ') || '-' }}</el-descriptions-item>
            <el-descriptions-item label="BMI">{{ userStore.profile?.profile?.bmi ?? '-' }}</el-descriptions-item>
            <el-descriptions-item label="BMR">{{ userStore.profile?.profile?.bmr ?? '-' }}</el-descriptions-item>
            <el-descriptions-item label="日目标热量">{{ userStore.profile?.profile?.dailyCalorieTarget ?? '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-col>

        <el-col :md="12" :span="24">
          <el-form ref="formRef" :model="form" :rules="rules" label-position="top" :disabled="!editing">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender">
                <el-option label="男" value="MALE" />
                <el-option label="女" value="FEMALE" />
              </el-select>
            </el-form-item>
            <el-form-item label="生日" prop="birthday">
              <el-date-picker v-model="form.birthday" type="date" value-format="YYYY-MM-DD" />
            </el-form-item>
            <el-form-item label="身高(cm)" prop="heightCm">
              <el-input-number v-model="form.heightCm" :min="100" :max="250" :precision="1" />
            </el-form-item>
            <el-form-item label="体重(kg)" prop="weightKg">
              <el-input-number v-model="form.weightKg" :min="25" :max="250" :precision="1" />
            </el-form-item>
            <el-form-item label="活动水平" prop="activityLevel">
              <el-select v-model="form.activityLevel">
                <el-option label="久坐" value="SEDENTARY" />
                <el-option label="轻度活动" value="LIGHT" />
                <el-option label="中度活动" value="MODERATE" />
                <el-option label="活跃" value="ACTIVE" />
                <el-option label="高强度活动" value="VERY_ACTIVE" />
              </el-select>
            </el-form-item>
            <el-form-item label="目标" prop="goalType">
              <el-select v-model="form.goalType">
                <el-option label="减脂" value="LOSE" />
                <el-option label="维持" value="MAINTAIN" />
                <el-option label="增重" value="GAIN" />
              </el-select>
            </el-form-item>
            <el-form-item v-if="editing">
              <el-button type="primary" :loading="saving" @click="submit">保存档案</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { updateProfileApi } from '@/api'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref<FormInstance>()
const editing = ref(false)
const saving = ref(false)
const form = reactive({
  gender: 'MALE' as 'MALE' | 'FEMALE',
  birthday: '',
  heightCm: 170,
  weightKg: 60,
  activityLevel: 'MODERATE' as 'SEDENTARY' | 'LIGHT' | 'MODERATE' | 'ACTIVE' | 'VERY_ACTIVE',
  goalType: 'MAINTAIN' as 'LOSE' | 'MAINTAIN' | 'GAIN',
})

const rules: FormRules<typeof form> = {
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  birthday: [{ required: true, message: '请选择生日', trigger: 'change' }],
  heightCm: [{ required: true, message: '请输入身高', trigger: 'blur' }],
  weightKg: [{ required: true, message: '请输入体重', trigger: 'blur' }],
  activityLevel: [{ required: true, message: '请选择活动水平', trigger: 'change' }],
  goalType: [{ required: true, message: '请选择目标', trigger: 'change' }],
}

watch(
  () => userStore.profile,
  (profile) => {
    const p = profile?.profile
    if (!p) {
      return
    }
    form.gender = (p.gender as 'MALE' | 'FEMALE') || 'MALE'
    form.birthday = p.birthday || ''
    form.heightCm = Number(p.heightCm || 170)
    form.weightKg = Number(p.weightKg || 60)
    form.activityLevel = (p.activityLevel as typeof form.activityLevel) || 'MODERATE'
    form.goalType = (p.goalType as typeof form.goalType) || 'MAINTAIN'
  },
  { immediate: true },
)

async function submit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  saving.value = true
  try {
    const data = await updateProfileApi(form)
    userStore.profile = data
    editing.value = false
    ElMessage.success('个人档案已更新')
  } finally {
    saving.value = false
  }
}
</script>
