import request from '@/utils/request'
import type { CheckinRecord, CommentRecord, GoalRecord, PageResponse, PostRecord } from '@/types/api'

export function createGoalApi(data: Omit<GoalRecord, 'id'>) {
  return request.post<never, GoalRecord>('/api/goals', data)
}

export function updateGoalApi(id: number, data: Omit<GoalRecord, 'id'>) {
  return request.put<never, GoalRecord>(`/api/goals/${id}`, data)
}

export function deleteGoalApi(id: number) {
  return request.delete<never, void>(`/api/goals/${id}`)
}

export function fetchGoalPageApi(params: { page: number; size: number }) {
  return request.get<never, PageResponse<GoalRecord>>('/api/goals/page', { params })
}

export function createCheckinApi(data: Omit<CheckinRecord, 'id'>) {
  return request.post<never, CheckinRecord>('/api/checkins', data)
}

export function fetchCheckinPageApi(params: { page: number; size: number }) {
  return request.get<never, PageResponse<CheckinRecord>>('/api/checkins/page', { params })
}

export function createPostApi(data: { content: string }) {
  return request.post<never, PostRecord>('/api/posts', data)
}

export function fetchPostPageApi(params: { page: number; size: number }) {
  return request.get<never, PageResponse<PostRecord>>('/api/posts/page', { params })
}

export function createCommentApi(postId: number, data: { content: string }) {
  return request.post<never, CommentRecord>(`/api/posts/${postId}/comments`, data)
}

export function likePostApi(postId: number) {
  return request.post<never, void>(`/api/posts/${postId}/likes`)
}

export function unlikePostApi(postId: number) {
  return request.delete<never, void>(`/api/posts/${postId}/likes`)
}

export function adminDeletePostApi(postId: number) {
  return request.delete<never, void>(`/api/admin/posts/${postId}`)
}
