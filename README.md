# MyOffice

基于 OnlyOffice 社区版的协同文档管理系统，支持 Word / Excel / PowerPoint 等格式文件的在线查看、编辑与多人协作。

## 技术栈

- **前端**：SvelteKit + TypeScript + TailwindCSS（Vite 构建，`@sveltejs/adapter-static` 输出 SPA），Nginx 静态托管 + 反向代理。
- **后端**：Java 17 + Spring Boot + Spring Security + JWT + MyBatis-Plus + Thymeleaf，集成 OnlyOffice Java SDK。
- **数据库**：MySQL 8.4。
- **文档服务**：OnlyOffice DocumentServer（社区版，JWT 鉴权）。
- **测试**：Spring Boot Test + JUnit 5 + Mockito。
- **部署**：Docker + Docker Compose（多阶段构建）。

## 目录结构

```
myoffice/
├── app-client/         # SvelteKit 前端
├── app-server/         # Spring Boot 后端
├── docker-compose.yml  # 四个服务（mysql / onlyoffice / app-server / app-client）的一键编排
└── .env.example        # 端口与密钥的可覆盖配置示例
```

## 快速启动（推荐：Docker Compose）

> 前置条件：本机已安装 Docker 与 Docker Compose v2。

```bash
# 可选：复制环境变量示例并按需修改
cp .env.example .env

# 构建并以后台模式启动全部服务
docker compose up -d --build
```

启动后默认端口：

| 服务                    | 容器端口 | 宿主机端口（默认）         |
| ----------------------- | -------- | -------------------------- |
| 前端（Nginx）           | 80       | `${APP_CLIENT_HOST_PORT:-3000}` |
| 后端（Spring Boot）     | 80       | `${APP_SERVER_HOST_PORT:-8080}` |
| OnlyOffice DocumentServer | 80     | `${ONLYOFFICE_HOST_PORT:-8088}` |
| MySQL                   | 3306     | `${MYSQL_HOST_PORT:-3306}` |

访问入口：

- 普通用户登录：<http://localhost:3000/login>
- 管理员登录：<http://localhost:3000/admin/login>

## 默认账号（开发环境种子数据）

后端首次启动会自动执行 `app-server/src/main/resources/data/data.sql` 初始化基础数据。

| 角色     | 用户名 | 密码     |
| -------- | ------ | -------- |
| 管理员   | `admin` | `admin123` |
| 普通用户 | `alice` / `bob` / `carol` / `david` / `erin` | `123456` |

## 本地开发（不使用 Docker）

### 后端

```bash
cd app-server
./mvnw spring-boot:run                 # 默认使用 application-dev.properties
```

要求本机已运行 MySQL 8.x 与 OnlyOffice DocumentServer，并按 `application-dev.properties` 调整连接信息。

### 前端

```bash
cd app-client
npm install
npm run dev                            # 默认监听 http://localhost:5173
```

Vite dev server 已配置将 `/api`、`/editor`、`/health` 反向代理到本机 80 端口的后端。

## 跨服务通信要点

- 后端与 OnlyOffice 必须共享同一 `JWT_SECRET`（`docker-compose.yml` 中通过 `ONLYOFFICE_JWT_SECRET` 注入）。
- OnlyOffice 回调后端时使用 Docker 内部网络主机名 `http://app-server:80`（通过 `MYOFFICE_INTERNAL_BASE_URL` 注入），浏览器侧仍通过宿主机映射端口访问。
- 前端 Nginx 将 `/api`、`/editor`、`/health` 反向代理到容器名 `app-server`。

## 常用维护命令

```bash
docker compose logs -f app-server         # 查看后端日志
docker compose restart app-server         # 重启后端
docker compose down                       # 停止并移除全部容器（保留数据卷）
docker compose down -v                    # 同时清空 MySQL / OnlyOffice 数据卷
```
