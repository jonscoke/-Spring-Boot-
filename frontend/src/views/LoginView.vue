<template>
  <div class="login-page">
    <section class="login-hero">
      <span class="login-hero__badge">Vue 3 + Vite + TypeScript</span>
      <h1>青春智慧运动健康系统</h1>
      <p>
        清爽、现代、可扩展的管理前端骨架已经搭建。当前页完成登录入口、主题氛围和基础认证流转。
      </p>
      <div class="login-hero__highlights">
        <div>
          <strong>Router Guard</strong>
          <span>自动鉴权与管理员菜单控制</span>
        </div>
        <div>
          <strong>Axios Layer</strong>
          <span>统一拦截器与 API 封装</span>
        </div>
        <div>
          <strong>Dashboard Ready</strong>
          <span>图表、布局、菜单已预留</span>
        </div>
      </div>
    </section>

    <section class="login-panel">
      <div class="login-card">
        <div class="login-card__head">
          <h2>登录系统</h2>
          <p>默认对接后端 `/api/auth/login`。</p>
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="handleSubmit">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
          </el-form-item>
          <el-form-item>
            <el-button class="login-card__button" type="primary" :loading="loading" @click="handleSubmit">
              登录
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-card__footer">
          <span>API Base</span>
          <code>{{ apiBaseUrl }}</code>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL
const form = reactive({
  username: 'admin',
  password: 'Admin@123456',
})

const rules: FormRules<typeof form> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  loading.value = true
  try {
    await userStore.login(form.username, form.password)
    ElMessage.success('登录成功')
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard'
    await router.replace(redirect)
  } finally {
    loading.value = false
  }
}
</script>
