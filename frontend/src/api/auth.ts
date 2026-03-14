import request from '@/utils/request'
import type { LoginPayload, RegisterPayload, UserProfile } from '@/types/api'

export function loginApi(data: { username: string; password: string }) {
  return request.post<never, LoginPayload>('/api/auth/login', data)
}

export function registerApi(data: RegisterPayload) {
  return request.post<never, void>('/api/auth/register', data)
}

export function fetchMeApi() {
  return request.get<never, UserProfile>('/api/auth/me')
}
