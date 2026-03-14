# 数据库设计草案（MySQL 8）

## 1. 设计约定
- 字符集：`utf8mb4`
- 主键：`bigint`（雪花 ID 或自增均可，原型建议自增）
- 时间字段：`created_at`、`updated_at`
- 逻辑删除：关键业务表保留 `is_deleted`
- 外键策略：开发原型阶段建议保留外键约束，确保数据一致性

## 2. 核心表清单

## 2.1 用户与权限域

### `sys_user`
- 主键：`id`
- 唯一索引：`uk_sys_user_username(username)`，`uk_sys_user_phone(phone)`，`uk_sys_user_email(email)`
- 核心字段：`username`，`password_hash`，`nickname`，`phone`，`email`，`status`，`last_login_at`

### `sys_role`
- 主键：`id`
- 唯一索引：`uk_sys_role_code(role_code)`
- 核心字段：`role_name`，`role_code`，`status`，`remark`

### `sys_permission`
- 主键：`id`
- 唯一索引：`uk_sys_perm_code(permission_code)`
- 核心字段：`permission_name`，`permission_code`，`resource`，`action`

### `sys_user_role`
- 主键：`id`
- 外键：`user_id -> sys_user.id`，`role_id -> sys_role.id`
- 唯一索引：`uk_sys_user_role(user_id, role_id)`
- 核心字段：`user_id`，`role_id`

### `sys_role_permission`
- 主键：`id`
- 外键：`role_id -> sys_role.id`，`permission_id -> sys_permission.id`
- 唯一索引：`uk_sys_role_perm(role_id, permission_id)`
- 核心字段：`role_id`，`permission_id`

## 2.2 健康档案域

### `user_health_profile`
- 主键：`id`
- 外键：`user_id -> sys_user.id`
- 唯一索引：`uk_health_profile_user(user_id)`
- 核心字段：`gender`，`birthday`，`height_cm`，`target_weight_kg`，`activity_level`，`medical_note`

### `health_metric_record`
- 主键：`id`
- 外键：`user_id -> sys_user.id`
- 普通索引：`idx_metric_user_type_time(user_id, metric_type, measured_at)`
- 核心字段：`metric_type`（weight/body_fat/heart_rate/blood_pressure 等），`metric_value`，`unit`，`measured_at`，`source`

## 2.3 运动域

### `exercise_type`
- 主键：`id`
- 唯一索引：`uk_exercise_type_code(type_code)`
- 核心字段：`type_name`，`type_code`，`met_value`，`calorie_formula_note`

### `exercise_record`
- 主键：`id`
- 外键：`user_id -> sys_user.id`，`exercise_type_id -> exercise_type.id`
- 普通索引：`idx_exercise_user_time(user_id, start_time)`
- 唯一索引（幂等可选）：`uk_exercise_source(user_id, source, source_record_id)`
- 核心字段：`duration_min`，`distance_km`，`calorie_burned`，`avg_heart_rate`，`start_time`，`end_time`，`source`

## 2.4 饮食域

### `food_item`
- 主键：`id`
- 唯一索引：`uk_food_name(food_name)`
- 核心字段：`food_name`，`food_category`，`calorie_per_100g`，`protein_per_100g`，`fat_per_100g`，`carb_per_100g`

### `diet_record`
- 主键：`id`
- 外键：`user_id -> sys_user.id`
- 普通索引：`idx_diet_user_date(user_id, meal_time)`
- 核心字段：`meal_type`（breakfast/lunch/dinner/snack），`meal_time`，`remark`

### `diet_record_item`
- 主键：`id`
- 外键：`diet_record_id -> diet_record.id`，`food_id -> food_item.id`
- 核心字段：`food_weight_g`，`calorie`，`protein`，`fat`，`carb`

## 2.5 建议与报表域

### `recommendation_rule`
- 主键：`id`
- 唯一索引：`uk_reco_rule_code(rule_code)`
- 核心字段：`rule_code`，`rule_name`，`rule_condition_json`，`rule_action_text`，`priority`，`status`

### `recommendation_log`
- 主键：`id`
- 外键：`user_id -> sys_user.id`，`rule_id -> recommendation_rule.id`
- 普通索引：`idx_reco_user_time(user_id, generated_at)`
- 核心字段：`recommendation_text`，`source_type`（rule/llm），`generated_at`

### `report_task`
- 主键：`id`
- 外键：`user_id -> sys_user.id`
- 普通索引：`idx_report_user_type_period(user_id, report_type, period_start, period_end)`
- 核心字段：`report_type`（weekly/monthly），`status`，`period_start`，`period_end`，`generated_at`

### `report_snapshot`
- 主键：`id`
- 外键：`report_task_id -> report_task.id`
- 唯一索引：`uk_report_snapshot_task(report_task_id)`
- 核心字段：`summary_json`，`chart_json`，`advice_text`

## 2.6 社交域

### `user_goal`
- 主键：`id`
- 外键：`user_id -> sys_user.id`
- 普通索引：`idx_goal_user_status(user_id, status)`
- 核心字段：`goal_type`，`goal_value`，`start_date`，`end_date`，`status`

### `checkin_record`
- 主键：`id`
- 外键：`user_id -> sys_user.id`，`goal_id -> user_goal.id`（可空）
- 唯一索引：`uk_checkin_user_date(user_id, checkin_date)`
- 核心字段：`checkin_date`，`content`，`mood_score`

### `social_post`
- 主键：`id`
- 外键：`user_id -> sys_user.id`
- 普通索引：`idx_post_created_at(created_at)`
- 核心字段：`content`，`images_json`，`visibility`，`like_count`，`comment_count`

### `social_comment`
- 主键：`id`
- 外键：`post_id -> social_post.id`，`user_id -> sys_user.id`，`parent_comment_id -> social_comment.id`（可空）
- 普通索引：`idx_comment_post_time(post_id, created_at)`
- 核心字段：`content`

### `social_like`
- 主键：`id`
- 外键：`post_id -> social_post.id`，`user_id -> sys_user.id`
- 唯一索引：`uk_like_post_user(post_id, user_id)`
- 核心字段：`post_id`，`user_id`

## 2.7 第三方同步域（mock）

### `third_party_account`
- 主键：`id`
- 外键：`user_id -> sys_user.id`
- 唯一索引：`uk_third_account(user_id, provider, external_user_id)`
- 核心字段：`provider`，`external_user_id`，`access_token`（密文），`refresh_token`（密文），`bind_status`

### `sync_cursor`
- 主键：`id`
- 外键：`account_id -> third_party_account.id`
- 唯一索引：`uk_sync_cursor(account_id, data_type)`
- 核心字段：`data_type`，`cursor_value`，`last_sync_at`

### `sync_job_log`
- 主键：`id`
- 外键：`account_id -> third_party_account.id`
- 普通索引：`idx_sync_job_status_time(status, started_at)`
- 核心字段：`job_type`，`status`，`started_at`，`finished_at`，`error_message`

### `sync_raw_event`
- 主键：`id`
- 外键：`account_id -> third_party_account.id`，`job_id -> sync_job_log.id`
- 唯一索引：`uk_sync_raw_dedup(provider, external_event_id, event_time)`
- 核心字段：`provider`，`external_event_id`，`event_time`，`payload_json`

### `sync_dedup_key`
- 主键：`id`
- 唯一索引：`uk_sync_dedup_key(dedup_key)`
- 核心字段：`dedup_key`，`biz_type`，`expire_at`

## 3. 关键关系图（文字版）
- 用户（`sys_user`）是一切业务数据的根实体，1:N 关联健康、运动、饮食、社交、报表、同步账户。
- 帖子（`social_post`）1:N 评论（`social_comment`），1:N 点赞（`social_like`）。
- 饮食记录（`diet_record`）1:N 明细（`diet_record_item`），明细 N:1 食物库（`food_item`）。
- 同步账户（`third_party_account`）1:N 同步任务（`sync_job_log`）与原始事件（`sync_raw_event`），并通过 `sync_cursor` 做增量游标控制。
