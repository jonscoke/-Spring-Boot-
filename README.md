# 基于 Spring Boot 的青春智慧运动健康系统（原型）

## 1. 项目简介
本项目为毕业设计原型工程，目标是构建一个面向青年用户的运动健康管理平台，覆盖健康档案、运动饮食记录、统计可视化、建议生成、报表与基础社交等功能。  
当前阶段为设计初始化阶段，仅输出架构与文档，不包含业务代码实现。

## 2. 固定技术栈
- 后端：Java 17、Spring Boot 3.x、Spring Security、MyBatis、MySQL 8、Redis 7
- 前端：Vue 3、Vite、Element Plus、Pinia、Axios、ECharts
- 鉴权：JWT
- 接口文档：OpenAPI
- 架构风格：前后端分离、分层单体、RESTful API
- 数据库初始化：Flyway（推荐，版本化迁移）
- 测试：后端 JUnit、前端至少保证 `build` 通过

## 3. 模块范围（原型）
1. 用户与权限管理
2. 用户健康档案
3. 运动记录
4. 饮食记录与食物库
5. 统计分析与可视化
6. 个性化建议（规则引擎，预留 LLM 接口）
7. 周报/月报
8. 基础社交（打卡、目标、发帖、评论、点赞）
9. 第三方运动数据同步（mock provider，幂等去重，增量同步）

## 4. 文档导航
- 需求范围与阶段边界：[docs/prototype-scope.md](./docs/prototype-scope.md)
- 系统架构设计：[docs/architecture.md](./docs/architecture.md)
- 数据库设计草案：[docs/db-design.md](./docs/db-design.md)
- API 清单草案：[docs/api-list.md](./docs/api-list.md)

## 5. 前后端目录结构草案
```text
project-root/
├─ backend/
│  ├─ src/main/java/com/youthhealth/
│  │  ├─ common/                 # 统一响应、异常、校验、工具
│  │  ├─ config/                 # 安全、MyBatis、Redis、OpenAPI 配置
│  │  ├─ security/               # JWT 过滤器、令牌服务、权限处理
│  │  ├─ module/
│  │  │  ├─ auth/
│  │  │  ├─ user/
│  │  │  ├─ health/
│  │  │  ├─ exercise/
│  │  │  ├─ diet/
│  │  │  ├─ analytics/
│  │  │  ├─ recommendation/
│  │  │  ├─ report/
│  │  │  ├─ social/
│  │  │  └─ sync/
│  │  └─ YouthHealthApplication.java
│  ├─ src/main/resources/
│  │  ├─ application.yml
│  │  ├─ application-dev.yml
│  │  ├─ application-prod.yml
│  │  ├─ db/migration/           # Flyway SQL 脚本
│  │  └─ mapper/                 # MyBatis XML
│  └─ src/test/java/
├─ frontend/
│  ├─ src/
│  │  ├─ api/
│  │  ├─ stores/
│  │  ├─ router/
│  │  ├─ views/
│  │  ├─ components/
│  │  ├─ layouts/
│  │  ├─ charts/
│  │  └─ utils/
│  ├─ public/
│  ├─ index.html
│  └─ vite.config.ts
└─ docs/
   ├─ prototype-scope.md
   ├─ architecture.md
   ├─ db-design.md
   └─ api-list.md
```

## 6. 本地启动方式（后续代码完成后）
当前仓库尚未进入业务代码实现阶段，以下为约定启动方式：

```bash
# 后端
cd backend
./mvnw spring-boot:run

# 前端
cd frontend
npm install
npm run dev
```

默认约定：
- 后端地址：`http://localhost:8080`
- 前端地址：`http://localhost:5173`
- OpenAPI：`http://localhost:8080/swagger-ui/index.html`

## 7. 开发阶段与里程碑（9 阶段）
1. 阶段 1：工程初始化与基础规范  
里程碑：前后端脚手架完成，后端可启动，前端可 `build`，统一返回/异常/校验与安全基础接入。
2. 阶段 2：用户与权限管理  
里程碑：注册登录、JWT 鉴权、RBAC、用户角色权限管理可用。
3. 阶段 3：健康档案管理  
里程碑：健康档案与体征记录 CRUD、趋势数据查询完成。
4. 阶段 4：运动记录管理  
里程碑：运动记录与运动类型管理、基础运动统计完成。
5. 阶段 5：饮食记录与食物库  
里程碑：食物库、饮食记录及营养汇总能力完成。
6. 阶段 6：统计分析与可视化  
里程碑：仪表盘统计 API 与 ECharts 页面完成。
7. 阶段 7：个性化建议与报表  
里程碑：规则建议生成、周报/月报生成与查询完成。
8. 阶段 8：基础社交  
里程碑：打卡、目标、帖子、评论、点赞闭环完成。
9. 阶段 9：第三方运动数据同步（mock）与收尾  
里程碑：增量同步、幂等去重、同步日志、系统联调与测试文档完成。

## 8. 说明
- 当前提交为设计层初始化文档，不包含 Controller/Service/前端页面等业务代码。
- 后续每轮仅推进一个阶段，阶段结束后进行运行命令与验证记录。
