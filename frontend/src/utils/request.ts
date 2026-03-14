import axios, { AxiosError } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse } from '@/types/api'
import { clearToken, getToken } from '@/utils/auth'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000,
})

request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => {
    const payload = response.data as ApiResponse<unknown>
    if (payload && typeof payload.code === 'number') {
      if (payload.code === 0) {
        return payload.data
      }
      ElMessage.error(payload.message || 'Request failed')
      return Promise.reject(new Error(payload.message || 'Request failed'))
    }
    return response.data
  },
  (error: AxiosError<{ message?: string }>) => {
    if (error.response?.status === 401) {
      clearToken()
    }
    ElMessage.error(error.response?.data?.message || error.message || 'Network error')
    return Promise.reject(error)
  },
)

export default request
