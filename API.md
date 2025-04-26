# 叮咚建议服务 API 文档

## 基础信息

- 基础URL: `https://springboot-wu96-152263-4-1352937363.sh.run.tcloudbase.com`
- 所有响应都遵循以下格式：
```json
{
    "code": 200,      // 状态码，200表示成功
    "message": "success",  // 响应消息
    "data": {}        // 响应数据，可能为null
}
```

## 用户管理

### 微信登录
- **URL**: `/api/users/login-by-wx-code`
- **方法**: POST
- **描述**: 使用微信code进行登录
- **请求体**:
```json
{
    "wxCode": "string"  // 微信登录code
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "string",
        "wxid": "string",
        "userPhone": "string",
        "token": "string"
    }
}
```

### 发送验证码
- **URL**: `/api/users/send-verification-code`
- **方法**: POST
- **描述**: 发送手机验证码
- **请求体**:
```json
{
    "phone": "string"  // 手机号
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": null
}
```

### 手机号注册
- **URL**: `/api/users/register-by-phone-and-code`
- **方法**: POST
- **描述**: 使用手机号和验证码进行注册
- **请求体**:
```json
{
    "phone": "string",           // 手机号
    "verificationCode": "string", // 验证码
    "wxCode": "string"           // 微信临时code
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "string",
        "wxid": "string",
        "userPhone": "string",
        "token": "string"
    }
}
```

### 获取用户组织
- **URL**: `/api/users/user-org`
- **方法**: POST
- **描述**: 根据JWT token获取用户所属的组织
- **请求体**:
```json
{
    "jwtToken": "string"  // JWT token
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": "string",
            "orgId": "string",
            "orgName": "string",
            "orgDesc": "string"
        }
    ]
}
```

## 组织管理

### 创建组织
- **URL**: `/api/organizations`
- **方法**: POST
- **描述**: 创建新的组织
- **请求体**:
```json
{
    "orgId": "string",
    "orgName": "string",
    "orgDesc": "string"
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "string",
        "orgId": "string",
        "orgName": "string",
        "orgDesc": "string"
    }
}
```

### 获取组织信息
- **URL**: `/api/organizations/{id}`
- **方法**: GET
- **描述**: 根据ID获取组织信息
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "string",
        "orgId": "string",
        "orgName": "string",
        "orgDesc": "string"
    }
}
```

### 根据组织ID获取组织信息
- **URL**: `/api/organizations/orgid/{orgId}`
- **方法**: GET
- **描述**: 根据组织ID获取组织信息
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "string",
        "orgId": "string",
        "orgName": "string",
        "orgDesc": "string"
    }
}
```

### 获取所有组织
- **URL**: `/api/organizations`
- **方法**: GET
- **描述**: 获取所有组织列表
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": "string",
            "orgId": "string",
            "orgName": "string",
            "orgDesc": "string"
        }
    ]
}
```

## 建议管理

### 创建建议
- **URL**: `/api/v1/suggestions`
- **方法**: POST
- **描述**: 创建新的建议
- **请求体**:
```json
{
    "title": "string",
    "content": "string",
    "orgId": "string",
    "jwtToken": "string"
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "number",
        "title": "string",
        "content": "string",
        "status": "string",
        "createdAt": "string"
    }
}
```

### 获取建议详情
- **URL**: `/api/v1/suggestions/{id}`
- **方法**: GET
- **描述**: 获取建议详情
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "number",
        "title": "string",
        "content": "string",
        "status": "string",
        "createdAt": "string"
    }
}
```

### 获取建议列表
- **URL**: `/api/v1/suggestions`
- **方法**: GET
- **描述**: 获取用户的所有建议列表
- **参数**:
  - `jwtToken`: JWT token
  - `orgId`: 组织ID
  - `year`: 年份
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": "number",
            "title": "string",
            "status": "string",
            "createdAt": "string"
        }
    ]
}
```

### 更新建议状态
- **URL**: `/api/v1/suggestions/{id}/status`
- **方法**: PUT
- **描述**: 更新建议的状态
- **参数**:
  - `status`: 新的状态值
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": null
}
```

## 管理员管理

### 创建管理员
- **URL**: `/api/admins`
- **方法**: POST
- **描述**: 创建新的管理员
- **请求体**:
```json
{
    "adminName": "string",
    "adminWxid": "string"
}
```
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "number",
        "adminName": "string",
        "adminWxid": "string"
    }
}
```

### 获取管理员信息
- **URL**: `/api/admins/{id}`
- **方法**: GET
- **描述**: 根据ID获取管理员信息
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "number",
        "adminName": "string",
        "adminWxid": "string"
    }
}
```

### 根据微信ID获取管理员
- **URL**: `/api/admins/wxid/{adminWxid}`
- **方法**: GET
- **描述**: 根据微信ID获取管理员信息
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "number",
        "adminName": "string",
        "adminWxid": "string"
    }
}
```

### 获取所有管理员
- **URL**: `/api/admins`
- **方法**: GET
- **描述**: 获取所有管理员列表
- **响应**:
```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": "number",
            "adminName": "string",
            "adminWxid": "string"
        }
    ]
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

