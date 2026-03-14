export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  traceId: string
  timestamp: string
}

export interface PageResponse<T> {
  total: number
  page: number
  size: number
  records: T[]
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

export interface RegisterPayload {
  username: string
  password: string
  nickname: string
  phone?: string
  email?: string
}

export interface ProfileFormPayload {
  gender: 'MALE' | 'FEMALE'
  birthday: string
  heightCm: number
  weightKg: number
  activityLevel: 'SEDENTARY' | 'LIGHT' | 'MODERATE' | 'ACTIVE' | 'VERY_ACTIVE'
  goalType: 'LOSE' | 'MAINTAIN' | 'GAIN'
}

export interface SportRecord {
  id?: number
  userId?: number
  sportType: string
  steps: number
  distanceKm: number
  durationMin: number
  pace?: number
  caloriesBurned?: number
  sourceType: string
  externalId?: string | null
  recordDate: string
  createdAt?: string
  updatedAt?: string
}

export interface SportSummary {
  startDate: string
  endDate: string
  recordCount: number
  totalSteps: number
  totalDistanceKm: number
  totalDurationMin: number
  totalCaloriesBurned: number
}

export interface SportSyncResult {
  sourceType: string
  totalMockCount: number
  insertedCount: number
  duplicateCount: number
  mockSamples: SportRecord[]
}

export interface FoodItem {
  id: number
  foodName: string
  foodCategory?: string
  unit: string
  caloriePer100g: number
  proteinPer100g: number
  fatPer100g: number
  carbPer100g: number
  status: number
}

export interface DietRecordItemPayload {
  foodItemId: number
  intakeGram: number
}

export interface DietRecordItem {
  id?: number
  dietRecordId?: number
  foodItemId: number
  foodName: string
  intakeGram: number
  calories: number
  protein: number
  fat: number
  carb: number
  createdAt?: string
}

export interface DietRecord {
  id: number
  userId?: number
  mealType: string
  recordDate: string
  totalCalories: number
  totalProtein: number
  totalFat: number
  totalCarb: number
  remark?: string
  createdAt?: string
  updatedAt?: string
}

export interface DietRecordVO {
  record: DietRecord
  items: DietRecordItem[]
}

export interface DietSummary {
  startDate: string
  endDate: string
  recordCount: number
  totalCalories: number
  totalProtein: number
  totalFat: number
  totalCarb: number
}

export interface NutritionRatio {
  startDate: string
  endDate: string
  proteinCalorieRatio: number
  fatCalorieRatio: number
  carbCalorieRatio: number
}

export interface TrendPointSport {
  date: string
  caloriesBurned: number
  steps: number
  durationMin: number
}

export interface TrendPointDiet {
  date: string
  caloriesIntake: number
  protein: number
  fat: number
  carb: number
}

export interface WeightPoint {
  date: string
  weightKg: number
}

export interface DashboardStats {
  todayIntakeCalories: number
  todayBurnCalories: number
  todayEnergyGap: number
  sportTrend7d: TrendPointSport[]
  dietTrend7d: TrendPointDiet[]
  weightTrend: WeightPoint[]
}

export interface AdviceRecord {
  id: number
  energyAdvice: string
  dietAdvice: string
  sportAdvice: string
  adviceText: string
  summaryJson: string
  sourceType: string
  createdAt: string
}

export interface ReportRecord {
  id: number
  reportType: 'WEEKLY' | 'MONTHLY'
  periodStart: string
  periodEnd: string
  title: string
  summaryJson: string
  reportText: string
  createdAt: string
}

export interface GoalRecord {
  id: number
  userId?: number
  goalType: string
  goalValue: number
  startDate: string
  endDate?: string | null
  status: string
  createdAt?: string
  updatedAt?: string
}

export interface CheckinRecord {
  id: number
  userId?: number
  checkinDate: string
  content: string
  moodScore?: number
  goalId?: number | null
  createdAt?: string
  updatedAt?: string
}

export interface PostRecord {
  id: number
  userId: number
  authorNickname: string
  content: string
  images?: string[]
  likeCount: number
  commentCount: number
  liked: boolean
  createdAt: string
}

export interface CommentRecord {
  id: number
  postId: number
  userId: number
  authorNickname: string
  content: string
  createdAt: string
}
