import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { fetchMeApi, loginApi } from '@/api'
import type { UserProfile } from '@/types/api'
import { clearToken, getToken, setToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const profile = ref<UserProfile | null>(null)
  const loaded = ref(false)

  const roles = computed(() => profile.value?.roles ?? [])
  const isAdmin = computed(() => roles.value.includes('ADMIN'))
  const isLoggedIn = computed(() => Boolean(token.value))

  async function login(username: string, password: string) {
    const data = await loginApi({ username, password })
    token.value = data.token
    setToken(data.token)
    profile.value = data.user
    loaded.value = true
  }

  async function fetchProfile() {
    if (!token.value) {
      loaded.value = true
      profile.value = null
      return null
    }
    const data = await fetchMeApi()
    profile.value = data
    loaded.value = true
    return data
  }

  async function bootstrap() {
    if (loaded.value) {
      return
    }
    try {
      await fetchProfile()
    } catch {
      logout()
    }
  }

  function logout() {
    token.value = ''
    profile.value = null
    loaded.value = true
    clearToken()
  }

  return {
    token,
    profile,
    roles,
    isAdmin,
    isLoggedIn,
    loaded,
    login,
    fetchProfile,
    bootstrap,
    logout,
  }
})
