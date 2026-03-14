import { createRouter, createWebHistory, type RouteLocationNormalized } from 'vue-router'
import { useUserStore } from '@/stores/user'

declare module 'vue-router' {
  interface RouteMeta {
    title: string
    requiresAuth?: boolean
    adminOnly?: boolean
    menu?: boolean
    icon?: string
  }
}

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { title: 'Login' },
    },
    {
      path: '/',
      component: () => import('@/layouts/MainLayout.vue'),
      redirect: '/dashboard',
      meta: { title: 'Main', requiresAuth: true },
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/dashboard/DashboardView.vue'),
          meta: { title: '仪表盘', requiresAuth: true, menu: true, icon: 'Odometer' },
        },
        {
          path: 'profile',
          name: 'profile',
          component: () => import('@/views/profile/ProfileView.vue'),
          meta: { title: '个人档案', requiresAuth: true, menu: true, icon: 'User' },
        },
        {
          path: 'sports',
          name: 'sports',
          component: () => import('@/views/sport/SportView.vue'),
          meta: { title: '运动管理', requiresAuth: true, menu: true, icon: 'Trophy' },
        },
        {
          path: 'diets',
          name: 'diets',
          component: () => import('@/views/diet/DietView.vue'),
          meta: { title: '饮食管理', requiresAuth: true, menu: true, icon: 'Bowl' },
        },
        {
          path: 'advice',
          name: 'advice',
          component: () => import('@/views/advice/AdviceView.vue'),
          meta: { title: '个性化建议', requiresAuth: true, menu: true, icon: 'MagicStick' },
        },
        {
          path: 'reports',
          name: 'reports',
          component: () => import('@/views/report/ReportView.vue'),
          meta: { title: '报告中心', requiresAuth: true, menu: true, icon: 'Document' },
        },
        {
          path: 'community',
          name: 'community',
          component: () => import('@/views/community/CommunityView.vue'),
          meta: { title: '社区互动', requiresAuth: true, menu: true, icon: 'ChatDotRound' },
        },
        {
          path: 'admin',
          name: 'admin',
          component: () => import('@/views/admin/AdminView.vue'),
          meta: { title: '管理端', requiresAuth: true, menu: true, adminOnly: true, icon: 'Setting' },
        },
      ],
    },
  ],
  scrollBehavior: () => ({ top: 0 }),
})

router.beforeEach(async (to: RouteLocationNormalized) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth) {
    await userStore.bootstrap()
    if (!userStore.isLoggedIn) {
      return { path: '/login', query: { redirect: to.fullPath } }
    }
    if (to.meta.adminOnly && !userStore.isAdmin) {
      return { path: '/dashboard' }
    }
  }
  if (to.path === '/login' && userStore.isLoggedIn) {
    return { path: '/dashboard' }
  }
  document.title = `${to.meta.title} | ${import.meta.env.VITE_APP_TITLE}`
  return true
})

export default router
