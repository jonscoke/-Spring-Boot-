import request from '@/utils/request'
import type { AdviceRecord, DashboardStats, PageResponse, ReportRecord } from '@/types/api'

export function fetchDashboardStatsApi() {
  return request.get<never, DashboardStats>('/api/stats/dashboard')
}

export function fetchLatestAdviceApi() {
  return request.get<never, AdviceRecord | null>('/api/advice/latest')
}

export function generateAdviceApi() {
  return request.post<never, AdviceRecord>('/api/advice/generate')
}

export function generateWeeklyReportApi(weekStartDate?: string) {
  return request.post<never, ReportRecord>('/api/reports/weekly/generate', weekStartDate ? { weekStartDate } : {})
}

export function generateMonthlyReportApi(reportMonth?: string) {
  return request.post<never, ReportRecord>('/api/reports/monthly/generate', reportMonth ? { reportMonth } : {})
}

export function fetchReportPageApi(params: { page: number; size: number; reportType?: string }) {
  return request.get<never, PageResponse<ReportRecord>>('/api/reports/page', { params })
}
