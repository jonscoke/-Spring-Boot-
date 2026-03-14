import request from '@/utils/request'
import type { ProfileFormPayload, UserProfile } from '@/types/api'

export function updateProfileApi(data: ProfileFormPayload) {
  return request.put<never, UserProfile>('/api/users/profile', data)
}
