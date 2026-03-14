# 系统架构设计（Architecture）

## 1. 总体架构
- 架构模式：前后端分离 + 分层单体
- 接口风格：RESTful，统一前缀 `/api/v1`
- 鉴权：JWT（无状态）+ Spring Security + RBAC
- 数据层：MyBatis + MySQL 8
- 缓存层：Redis 7
- 文档：OpenAPI（Swagger UI）

## 2. 后端分层
1. Controller 层：接收请求、参数校验、响应封装
2. Service 层：业务编排、事务边界、规则执行
3. Mapper 层：MyBatis 数据访问
4. Domain/Entity 层：实体对象与领域模型
5. DTO/VO 层：输入输出契约
6. Common 层：统一返回、异常、工具、常量

## 3. 前端分层
1. 页面层（views）：模块化页面
2. 组件层（components/charts）：复用组件与图表
3. 状态层（Pinia stores）：用户态、缓存态
4. 请求层（api）：Axios 封装与接口定义
5. 路由层（router）：鉴权路由与页面导航

## 4. 目录结构草案
```text
project-root/
├─ backend/
│  ├─ src/main/java/com/youthhealth/
│  │  ├─ common/
│  │  ├─ config/
│  │  ├─ security/
│  │  ├─ module/auth/
│  │  ├─ module/user/
│  │  ├─ module/health/
│  │  ├─ module/exercise/
│  │  ├─ module/diet/
│  │  ├─ module/analytics/
│  │  ├─ module/recommendation/
│  │  ├─ module/report/
│  │  ├─ module/social/
│  │  └─ module/sync/
│  ├─ src/main/resources/
│  │  ├─ application*.yml
│  │  ├─ db/migration/
│  │  └─ mapper/
│  └─ src/test/java/
├─ frontend/
│  ├─ src/api/
│  ├─ src/stores/
│  ├─ src/router/
│  ├─ src/views/
│  ├─ src/components/
│  ├─ src/layouts/
│  ├─ src/charts/
│  └─ src/utils/
└─ docs/
```

## 5. 安全设计要点
- 登录后签发 Access Token（JWT），可选 Refresh Token
- 接口按角色与权限点控制访问
- 密码采用强哈希（BCrypt）
- 敏感操作记录审计日志
- 防重复提交与幂等控制（同步场景强制）

## 6. 一致性与规范
- 统一响应体：`code`、`message`、`data`、`traceId`、`timestamp`
- 统一异常：业务异常、参数异常、鉴权异常集中处理
- 统一校验：`jakarta.validation` 注解 + 全局处理
- 统一时间与时区策略：数据库 `datetime`，应用层统一处理

## 7. 数据同步策略（mock provider）
- 绑定关系：用户与第三方账户映射
- 增量游标：按 `cursor/last_sync_time` 分批拉取
- 幂等去重：基于 `provider + external_id + event_time` 唯一键
- 失败重试：任务日志记录状态，支持手动重放
