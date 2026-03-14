export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  traceId: string
  timestamp: string
}

export interface LoginPayload {
  token: string
  tokenType: string
  expiresIn: number
  user: UserProfile
}

export interface UserProfile {
  id: number
  username: string
  nickname: string
  phone?: string
  email?: string
  roles: string[]
  profile?: {
    gender?: string
    birthday?: string
    heightCm?: number
    weightKg?: number
    activityLevel?: string
    goalType?: string
    bmi?: number
    bmr?: number
    dailyCalorieTarget?: number
  }
}
