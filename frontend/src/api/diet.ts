import request from '@/utils/request'
import type {
  DietRecordVO,
  DietSummary,
  FoodItem,
  NutritionRatio,
  PageResponse,
} from '@/types/api'

export function fetchFoodPageApi(params: { page: number; size: number; keyword?: string }) {
  return request.get<never, PageResponse<FoodItem>>('/api/foods/page', { params })
}

export function searchFoodApi(keyword: string) {
  return request.get<never, FoodItem[]>('/api/foods/search', { params: { keyword } })
}

export function createDietApi(data: {
  mealType: string
  recordDate: string
  remark?: string
  items: { foodItemId: number; intakeGram: number }[]
}) {
  return request.post<never, DietRecordVO>('/api/diets', data)
}

export function fetchDietPageApi(params: { page: number; size: number; startDate?: string; endDate?: string }) {
  return request.get<never, PageResponse<DietRecordVO>>('/api/diets/page', { params })
}

export function fetchDietWeeklySummaryApi() {
  return request.get<never, DietSummary>('/api/diets/summary/weekly')
}

export function fetchNutritionRatioApi(params?: { startDate?: string; endDate?: string }) {
  return request.get<never, NutritionRatio>('/api/diets/nutrition-ratio', { params })
}
