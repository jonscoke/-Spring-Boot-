# API 清单草案（/api/v1）

## 1. 说明
- 本文档为原型阶段 API 草案，后续按阶段细化字段与返回结构。
- 统一响应：`{ code, message, data, traceId, timestamp }`
- 鉴权方式：除登录注册外，默认需要 `Authorization: Bearer <token>`

## 2. 认证与权限

| 方法 | 路径 | 作用 | 主要参数 |
|---|---|---|---|
| POST | `/auth/register` | 用户注册 | body: `username,password,phone,email` |
| POST | `/auth/login` | 用户登录并签发 JWT | body: `username,password` |
| POST | `/auth/refresh` | 刷新访问令牌 | body: `refreshToken` |
| POST | `/auth/logout` | 退出登录 | header: `Authorization` |
| GET | `/users/me` | 获取当前用户信息 | 无 |
| PUT | `/users/me` | 更新当前用户信息 | body: `nickname,avatar,phone,email` |
| GET | `/users` | 用户列表查询（管理员） | query: `page,size,keyword,status` |
| POST | `/users` | 新增用户（管理员） | body: `username,password,roleIds,status` |
| PUT | `/users/{id}` | 修改用户（管理员） | path: `id`, body: `nickname,status,roleIds` |
| DELETE | `/users/{id}` | 删除用户（管理员） | path: `id` |
| GET | `/roles` | 角色列表 | query: `page,size,keyword` |
| POST | `/roles` | 新增角色 | body: `roleName,roleCode,permissionIds` |
| PUT | `/roles/{id}` | 修改角色 | path: `id`, body: `roleName,status,permissionIds` |
| DELETE | `/roles/{id}` | 删除角色 | path: `id` |
| GET | `/permissions` | 权限列表 | query: `page,size,keyword` |

## 3. 健康档案

| 方法 | 路径 | 作用 | 主要参数 |
|---|---|---|---|
| GET | `/health/profile` | 获取个人健康档案 | 无 |
| PUT | `/health/profile` | 更新健康档案 | body: `gender,birthday,heightCm,targetWeightKg,activityLevel,medicalNote` |
| GET | `/health/metrics` | 体征记录列表 | query: `metricType,startDate,endDate,page,size` |
| POST | `/health/metrics` | 新增体征记录 | body: `metricType,metricValue,unit,measuredAt,source` |
| PUT | `/health/metrics/{id}` | 修改体征记录 | path: `id`, body: 同新增 |
| DELETE | `/health/metrics/{id}` | 删除体征记录 | path: `id` |
| GET | `/health/metrics/trend` | 体征趋势数据 | query: `metricType,period(week/month/quarter)` |

## 4. 运动记录

| 方法 | 路径 | 作用 | 主要参数 |
|---|---|---|---|
| GET | `/exercise/types` | 运动类型列表 | query: `keyword,status` |
| POST | `/exercise/types` | 新增运动类型（管理员） | body: `typeName,typeCode,metValue` |
| GET | `/exercise/records` | 运动记录列表 | query: `startDate,endDate,typeId,page,size` |
| POST | `/exercise/records` | 新增运动记录 | body: `exerciseTypeId,durationMin,distanceKm,calorieBurned,startTime,endTime,source` |
| PUT | `/exercise/records/{id}` | 修改运动记录 | path: `id`, body: 同新增 |
| DELETE | `/exercise/records/{id}` | 删除运动记录 | path: `id` |
| GET | `/exercise/stats/daily` | 按日统计 | query: `startDate,endDate` |
| GET | `/exercise/stats/monthly` | 按月统计 | query: `year` |

## 5. 饮食记录与食物库

| 方法 | 路径 | 作用 | 主要参数 |
|---|---|---|---|
| GET | `/foods` | 食物库查询 | query: `keyword,category,page,size` |
| POST | `/foods` | 新增食物（管理员） | body: `foodName,foodCategory,caloriePer100g,proteinPer100g,fatPer100g,carbPer100g` |
| PUT | `/foods/{id}` | 修改食物（管理员） | path: `id`, body: 同新增 |
| DELETE | `/foods/{id}` | 删除食物（管理员） | path: `id` |
| GET | `/diet/records` | 饮食记录列表 | query: `startDate,endDate,mealType,page,size` |
| POST | `/diet/records` | 新增饮食记录 | body: `mealType,mealTime,remark,items[]` |
| PUT | `/diet/records/{id}` | 修改饮食记录 | path: `id`, body: `mealType,mealTime,remark,items[]` |
| DELETE | `/diet/records/{id}` | 删除饮食记录 | path: `id` |
| GET | `/diet/stats/nutrition` | 营养汇总统计 | query: `startDate,endDate,groupBy(day/week/month)` |

## 6. 统计分析

| 方法 | 路径 | 作用 | 主要参数 |
|---|---|---|---|
| GET | `/analytics/dashboard/overview` | 仪表盘概览 | query: `period` |
| GET | `/analytics/trends` | 趋势分析 | query: `metric,startDate,endDate` |
| GET | `/analytics/compare` | 对比分析 | query: `metric,periodAStart,periodAEnd,periodBStart,periodBEnd` |

## 7. 个性化建议与报表

| 方法 | 路径 | 作用 | 主要参数 |
|---|---|---|---|
| POST | `/recommendations/generate` | 触发建议生成（规则） | body: `scene(optional)` |
| GET | `/recommendations/latest` | 获取最新建议 | 无 |
| GET | `/recommendations/history` | 建议历史 | query: `page,size,startDate,endDate` |
| POST | `/recommendations/llm/preview` | LLM 接口占位预览 | body: `promptContext` |
| POST | `/reports/weekly/generate` | 生成周报任务 | body: `weekStartDate` |
| POST | `/reports/monthly/generate` | 生成月报任务 | body: `month(yyyy-MM)` |
| GET | `/reports` | 报表列表 | query: `reportType,status,page,size` |
| GET | `/reports/{id}` | 报表详情 | path: `id` |

## 8. 基础社交

| 方法 | 路径 | 作用 | 主要参数 |
|---|---|---|---|
| GET | `/goals` | 目标列表 | query: `status,page,size` |
| POST | `/goals` | 新建目标 | body: `goalType,goalValue,startDate,endDate` |
| PUT | `/goals/{id}` | 更新目标 | path: `id`, body: `goalValue,status,endDate` |
| POST | `/checkins` | 每日打卡 | body: `checkinDate,content,moodScore,goalId(optional)` |
| GET | `/checkins/mine` | 我的打卡列表 | query: `startDate,endDate,page,size` |
| GET | `/posts` | 帖子列表 | query: `page,size,keyword,sort` |
| POST | `/posts` | 发布帖子 | body: `content,images[]` |
| GET | `/posts/{id}` | 帖子详情 | path: `id` |
| DELETE | `/posts/{id}` | 删除帖子 | path: `id` |
| POST | `/posts/{id}/comments` | 发表评论 | path: `id`, body: `content,parentCommentId(optional)` |
| GET | `/posts/{id}/comments` | 评论列表 | path: `id`, query: `page,size` |
| POST | `/posts/{id}/likes` | 点赞帖子 | path: `id` |
| DELETE | `/posts/{id}/likes` | 取消点赞 | path: `id` |

## 9. 第三方同步（mock provider）

| 方法 | 路径 | 作用 | 主要参数 |
|---|---|---|---|
| POST | `/sync/providers/mock/bind` | 绑定 mock 账号 | body: `externalUserId,authCode` |
| POST | `/sync/providers/mock/pull` | 触发增量拉取 | body: `accountId,dataType,forceFull(optional)` |
| GET | `/sync/jobs` | 同步任务列表 | query: `status,provider,page,size` |
| GET | `/sync/jobs/{id}` | 同步任务详情 | path: `id` |
| POST | `/sync/jobs/{id}/retry` | 失败任务重试 | path: `id` |

## 10. 通用查询参数约定
- 分页：`page`（从 1 开始）、`size`
- 时间区间：`startDate`、`endDate`（ISO 日期或日期时间）
- 排序：`sortBy`、`sortOrder(asc/desc)`
