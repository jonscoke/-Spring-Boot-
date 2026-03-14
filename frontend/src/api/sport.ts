import request from '@/utils/request'
import type { PageResponse, SportRecord, SportSummary, SportSyncResult } from '@/types/api'

export function fetchSportPageApi(params: { page: number; size: number; startDate?: string; endDate?: string }) {
  return request.get<never, PageResponse<SportRecord>>('/api/sports/page', { params })
}

export function createSportApi(data: SportRecord) {
  return request.post<never, SportRecord>('/api/sports', data)
}

export function updateSportApi(id: number, data: SportRecord) {
  return request.put<never, SportRecord>(`/api/sports/${id}`, data)
}

export function deleteSportApi(id: number) {
  return request.delete<never, void>(`/api/sports/${id}`)
}

export function fetchSportWeeklySummaryApi() {
  return request.get<never, SportSummary>('/api/sports/summary/weekly')
}

export function syncMockSportApi(sourceType = 'MOCK_APP') {
  return request.post<never, SportSyncResult>('/api/sports/sync/mock', { sourceType })
}
