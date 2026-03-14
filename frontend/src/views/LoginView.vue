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
          <p>登录和注册都已经对接后端认证接口。</p>
        </div>

        <el-tabs v-model="activeTab" stretch>
          <el-tab-pane label="登录" name="login">
            <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" size="large" @keyup.enter="handleLogin">
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" placeholder="请输入用户名" />
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" />
              </el-form-item>
              <el-form-item>
                <el-button class="login-card__button" type="primary" :loading="loading" @click="handleLogin">
                  登录
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="注册" name="register">
            <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" size="large">
              <el-form-item prop="username">
                <el-input v-model="registerForm.username" placeholder="用户名，4-20 位" />
              </el-form-item>
              <el-form-item prop="nickname">
                <el-input v-model="registerForm.nickname" placeholder="昵称" />
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="registerForm.password" type="password" show-password placeholder="密码" />
              </el-form-item>
              <el-form-item prop="phone">
                <el-input v-model="registerForm.phone" placeholder="手机号，可选" />
              </el-form-item>
              <el-form-item prop="email">
                <el-input v-model="registerForm.email" placeholder="邮箱，可选" />
              </el-form-item>
              <el-form-item>
                <el-button class="login-card__button" type="primary" :loading="registering" @click="handleRegister">
                  注册
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>

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
import { registerApi } from '@/api'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const registerFormRef = ref<FormInstance>()
const activeTab = ref('login')
const loading = ref(false)
const registering = ref(false)
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL
const loginForm = reactive({
  username: 'admin',
  password: 'Admin@123456',
})
const registerForm = reactive({
  username: '',
  nickname: '',
  password: '',
  phone: '',
  email: '',
})

const loginRules: FormRules<typeof loginForm> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const registerRules: FormRules<typeof registerForm> = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度需为 4-20 位', trigger: 'blur' },
  ],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await loginFormRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  loading.value = true
  try {
    await userStore.login(loginForm.username, loginForm.password)
    ElMessage.success('登录成功')
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard'
    await router.replace(redirect)
  } finally {
    loading.value = false
  }
}

async function handleRegister() {
  const valid = await registerFormRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  registering.value = true
  try {
    await registerApi({
      username: registerForm.username,
      nickname: registerForm.nickname,
      password: registerForm.password,
      phone: registerForm.phone || undefined,
      email: registerForm.email || undefined,
    })
    ElMessage.success('注册成功，请登录')
    activeTab.value = 'login'
    loginForm.username = registerForm.username
    loginForm.password = registerForm.password
    registerFormRef.value?.resetFields()
  } finally {
    registering.value = false
  }
}
</script>
