<template>
  <div class="shell">
    <aside class="shell__aside">
      <div class="brand">
        <div class="brand__mark">YH</div>
        <div>
          <div class="brand__title">Youth Health</div>
          <div class="brand__subtitle">Smart Wellness Console</div>
        </div>
      </div>

      <el-scrollbar class="shell__menu-scroll">
        <el-menu :default-active="activePath" class="shell__menu" router>
          <el-menu-item v-for="item in visibleMenuItems" :key="item.path" :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.title }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>

      <div class="shell__profile">
        <div class="shell__avatar">{{ userInitial }}</div>
        <div class="shell__profile-text">
          <strong>{{ userStore.profile?.nickname || '访客用户' }}</strong>
          <span>{{ userStore.isAdmin ? '管理员' : '普通用户' }}</span>
        </div>
      </div>
    </aside>

    <section class="shell__main">
      <header class="shell__header">
        <div>
          <h1>{{ currentTitle }}</h1>
          <p>前端框架与菜单已就位，后续页面数据可以逐步接入。</p>
        </div>
        <div class="shell__header-actions">
          <el-tag effect="dark" round type="success">{{ apiBaseUrl }}</el-tag>
          <el-button plain @click="handleLogout">退出登录</el-button>
        </div>
      </header>

      <main class="shell__content">
        <RouterView />
      </main>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute, useRouter } from 'vue-router'
import {
  Bowl,
  ChatDotRound,
  Document,
  MagicStick,
  Odometer,
  Setting,
  Trophy,
  User,
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

interface MenuItem {
  path: string
  title: string
  icon: typeof Odometer
  adminOnly?: boolean
}

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const menuItems: MenuItem[] = [
  { path: '/dashboard', title: '仪表盘', icon: Odometer },
  { path: '/profile', title: '个人档案', icon: User },
  { path: '/sports', title: '运动管理', icon: Trophy },
  { path: '/diets', title: '饮食管理', icon: Bowl },
  { path: '/advice', title: '个性化建议', icon: MagicStick },
  { path: '/reports', title: '报告中心', icon: Document },
  { path: '/community', title: '社区互动', icon: ChatDotRound },
  { path: '/admin', title: '管理端', icon: Setting, adminOnly: true },
]

const visibleMenuItems = computed(() => menuItems.filter((item) => !item.adminOnly || userStore.isAdmin))
const currentTitle = computed(() => route.meta.title || '管理台')
const activePath = computed(() => route.path)
const apiBaseUrl = computed(() => import.meta.env.VITE_API_BASE_URL)
const userInitial = computed(() => (userStore.profile?.nickname || 'Y').slice(0, 1).toUpperCase())

function handleLogout() {
  userStore.logout()
  void router.replace('/login')
}
</script>
