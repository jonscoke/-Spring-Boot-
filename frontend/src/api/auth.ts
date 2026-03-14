import request from '@/utils/request'
import type { LoginPayload, UserProfile } from '@/types/api'

export function loginApi(data: { username: string; password: string }) {
  return request.post<never, LoginPayload>('/api/auth/login', data)
}

export function fetchMeApi() {
  return request.get<never, UserProfile>('/api/auth/me')
}
