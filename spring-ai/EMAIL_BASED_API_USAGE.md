# 基于邮箱的个人中心API使用指南

## 概述

个人中心的所有API现在都已修改为基于用户邮箱地址进行身份验证和数据获取，而不是使用用户ID。这样可以更方便地与用户登录系统集成。

## API变更说明

### 请求头变更
- **原来**: `User-Id: 123`
- **现在**: `User-Email: user@example.com`

### 支持的API端点

#### 1. 获取用户个人信息
```
GET /api/me
Headers: User-Email: user@example.com


#### 2. 更新用户个人信息
```
PUT /api/me
Headers: User-Email: user@example.com
Content-Type: application/json


#### 3. 获取用户喜欢的节目
```
GET /api/me/liked-programs?page=1&limit=10
Headers: User-Email: user@example.com
```

#### 4. 获取用户的歌单
```
GET /api/me/playlists
Headers: User-Email: user@example.com
```

#### 5. 获取用户的评论
```
GET /api/me/comments?page=1&limit=10
Headers: User-Email: user@example.com
```

## 注意事项

1. **邮箱验证**: 确保传递的邮箱地址是有效且已注册的
2. **请求头**: 所有请求都必须包含 `User-Email` 请求头
3. **错误处理**: 建议在前端实现统一的错误处理机制
4. **安全性**: 在生产环境中，建议使用JWT等更安全的身份验证方式

## 迁移指南

如果您之前使用的是基于用户ID的API，需要进行以下修改：

1. 将所有 `User-Id` 请求头改为 `User-Email`
2. 确保您的前端应用能够获取到用户的邮箱地址
3. 更新错误处理逻辑以适应新的错误消息
4. 测试所有相关功能以确保正常工作
