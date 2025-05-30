# 头像上传功能 API 文档

## 概述

本文档描述了头像上传功能的完整后端实现，包括文件上传、用户资料更新等相关接口。

## 功能特性

- ✅ 支持 JPG、PNG、GIF 格式图片上传
- ✅ 文件大小限制（5MB以内）
- ✅ 安全的文件名生成
- ✅ 自动删除旧头像文件
- ✅ 完整的错误处理
- ✅ 统一的JSON响应格式

## API 接口

### 1. 头像上传接口

**接口地址：** `POST /api/upload/avatar`

**功能描述：** 上传用户头像图片文件

**请求方式：** `multipart/form-data`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| file | MultipartFile | 是 | 头像图片文件 |

**文件要求：**
- 支持格式：JPG、JPEG、PNG、GIF
- 文件大小：最大 5MB
- 文件名：自动生成安全文件名

**请求示例：**
```javascript
const formData = new FormData();
formData.append('file', avatarFile);

fetch('/api/upload/avatar', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => {
  if (data.success) {
    console.log('上传成功:', data.data.fileUrl);
  }
});
```

**响应格式：**
```json
{
  "code": 200,
  "message": "头像上传成功",
  "success": true,
  "data": {
    "fileName": "avatar_1640995200000_123.jpg",
    "fileUrl": "/uploads/avatars/avatar_1640995200000_123.jpg",
    "fileSize": 102400,
    "fileType": "image/jpeg",
    "uploadTime": 1640995200000
  }
}
```

**错误响应示例：**
```json
{
  "code": 400,
  "message": "只支持JPG、PNG、GIF格式的图片文件",
  "success": false,
  "data": null
}
```

### 2. 用户资料更新接口（支持头像上传）

**接口地址：** `POST /api/me/update-with-avatar`

**功能描述：** 更新用户个人资料，支持同时上传头像

**请求方式：** `multipart/form-data`

**请求头：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| User-Email | String | 是 | 用户邮箱地址 |

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 否 | 用户名 |
| bio | String | 否 | 个人简介 |
| avatar | MultipartFile | 否 | 头像图片文件 |

**请求示例：**
```javascript
const formData = new FormData();
formData.append('username', '新用户名');
formData.append('bio', '这是我的新简介');
if (avatarFile) {
  formData.append('avatar', avatarFile);
}

fetch('/api/me/update-with-avatar', {
  method: 'POST',
  headers: {
    'User-Email': 'user@example.com'
  },
  body: formData
})
.then(response => response.json())
.then(data => {
  if (data.success) {
    console.log('更新成功:', data.data);
  }
});
```

**响应格式：**
```json
{
  "code": 200,
  "message": "更新用户信息成功",
  "success": true,
  "data": {
    "id": 123,
    "username": "新用户名",
    "email": "user@example.com",
    "avatar": "/uploads/avatars/avatar_1640995200000_123.jpg",
    "bio": "这是我的新简介",
    "createdAt": "2024-01-15T10:30:00",
    "likedProgramsCount": 15,
    "playlistsCount": 3,
    "commentsCount": 8
  }
}
```

### 3. 删除头像接口

**接口地址：** `DELETE /api/upload/avatar/{filename}`

**功能描述：** 删除指定的头像文件

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| filename | String | 是 | 要删除的文件名 |

**响应格式：**
```json
{
  "code": 200,
  "message": "头像删除成功",
  "success": true,
  "data": true
}
```

### 4. 检查文件存在接口

**接口地址：** `GET /api/upload/avatar/{filename}/exists`

**功能描述：** 检查指定的头像文件是否存在

**响应格式：**
```json
{
  "code": 200,
  "message": "文件存在",
  "success": true,
  "data": true
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 400 | 请求参数错误（文件为空、格式不支持、大小超限等） |
| 404 | 用户不存在或文件不存在 |
| 500 | 服务器内部错误（文件保存失败等） |

## 安全性说明

1. **文件类型验证**：严格验证文件MIME类型和扩展名
2. **文件大小限制**：限制上传文件大小为5MB以内
3. **安全文件名**：自动生成安全的文件名，防止路径遍历攻击
4. **旧文件清理**：更新头像时自动删除旧的头像文件
5. **目录隔离**：头像文件存储在专门的目录中

## 配置说明

在 `application.properties` 中的相关配置：

```properties
# 文件上传配置
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
spring.servlet.multipart.file-size-threshold=2KB

# 文件存储路径配置
file.upload.path=src/main/resources/static/uploads/
file.upload.avatar-path=src/main/resources/static/uploads/avatars/
file.upload.base-url=/uploads/
```

## 前端集成示例

```html
<!-- HTML表单 -->
<form id="avatarForm" enctype="multipart/form-data">
  <input type="file" id="avatarInput" accept="image/*" />
  <input type="text" id="usernameInput" placeholder="用户名" />
  <textarea id="bioInput" placeholder="个人简介"></textarea>
  <button type="submit">更新资料</button>
</form>

<script>
document.getElementById('avatarForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  
  const formData = new FormData();
  const avatarFile = document.getElementById('avatarInput').files[0];
  const username = document.getElementById('usernameInput').value;
  const bio = document.getElementById('bioInput').value;
  
  if (username) formData.append('username', username);
  if (bio) formData.append('bio', bio);
  if (avatarFile) formData.append('avatar', avatarFile);
  
  try {
    const response = await fetch('/api/me/update-with-avatar', {
      method: 'POST',
      headers: {
        'User-Email': 'user@example.com' // 实际应用中从登录状态获取
      },
      body: formData
    });
    
    const result = await response.json();
    if (result.success) {
      alert('更新成功！');
      // 更新页面显示
      if (result.data.avatar) {
        document.getElementById('userAvatar').src = result.data.avatar;
      }
    } else {
      alert('更新失败：' + result.message);
    }
  } catch (error) {
    alert('网络错误：' + error.message);
  }
});
</script>
```
