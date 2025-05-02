# 叮咚建议服务 API 文档

## 基础信息

- 基础URL: `http://localhost:8080`
- 所有请求和响应均使用 JSON 格式
- 认证方式: Bearer Token (JWT)

## 认证相关

### 获取 Token

```http
POST /api/v1/auth/token
Content-Type: application/json

{
    "wxid": "string"  // 微信用户的唯一标识
}
```

响应:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "token": "string",  // JWT token，需要保存用于后续请求
        "expiresIn": 7200   // token 有效期，单位：秒
    }
}
```

### 验证 Token

```http
POST /api/v1/auth/verify
Authorization: Bearer <jwtToken>
```

响应:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "valid": true,
        "wxid": "string"
    }
}
```

> 认证说明：
> 1. 小程序端需要先调用获取 Token 接口，获取 `jwtToken`
> 2. 获取到 token 后，需要将其保存在本地存储中（如 wx.setStorageSync('jwtToken', token)）
> 3. 在后续所有需要认证的接口中，都需要在请求头中添加 `Authorization: Bearer <jwtToken>`
> 4. 示例代码：
> ```javascript
> // 保存 token
> wx.setStorageSync('jwtToken', response.data.token);
> 
> // 使用 token 发送请求
> wx.request({
>   url: 'http://localhost:8080/api/v1/suggestions',
>   header: {
>     'Authorization': 'Bearer ' + wx.getStorageSync('jwtToken')
>   },
>   // ... 其他请求配置
> });
> ```

## 建议相关

### 创建建议

```http
POST /api/v1/suggestions
Authorization: Bearer <jwtToken>
Content-Type: application/json

{
    "content": "string",  // 建议内容
    "type": "string"      // 建议类型
}
```

响应:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "string",
        "content": "string",
        "type": "string",
        "status": "PENDING",
        "createdAt": "string",
        "updatedAt": "string"
    }
}
```

### 获取建议列表

```http
GET /api/v1/suggestions
Authorization: Bearer <jwtToken>
```

查询参数:
- `page`: 页码，从0开始，默认0
- `size`: 每页大小，默认10
- `type`: 建议类型（可选）
- `status`: 建议状态（可选）

响应:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "content": [
            {
                "id": "string",
                "content": "string",
                "type": "string",
                "status": "PENDING",
                "createdAt": "string",
                "updatedAt": "string"
            }
        ],
        "totalElements": 0,    // 总记录数
        "totalPages": 0,       // 总页数
        "size": 10,            // 每页大小
        "number": 0,           // 当前页码
        "first": true,         // 是否第一页
        "last": true,          // 是否最后一页
        "empty": true          // 是否为空
    }
}
```

### 获取建议详情

```http
GET /api/v1/suggestions/{id}
Authorization: Bearer <jwtToken>
```

响应:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "string",
        "content": "string",
        "type": "string",
        "status": "PENDING",
        "createdAt": "string",
        "updatedAt": "string"
    }
}
```

### 更新建议状态

```http
PATCH /api/v1/suggestions/{id}/status
Authorization: Bearer <jwtToken>
Content-Type: application/json

{
    "status": "string"  // 新的状态值
}
```

响应:
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": "string",
        "content": "string",
        "type": "string",
        "status": "PENDING",
        "createdAt": "string",
        "updatedAt": "string"
    }
}
```

## 错误响应

所有接口在发生错误时都会返回以下格式:

```json
{
    "code": 400,
    "message": "错误信息",
    "data": null
}
```

常见错误码:
- 400: 请求参数错误
- 401: 未认证或认证失败（token 无效或过期）
- 403: 权限不足
- 404: 资源不存在
- 500: 服务器内部错误

> 小程序端错误处理建议：
> 1. 当收到 401 错误时，说明 token 已过期或无效，需要重新获取 token
> 2. 建议在请求拦截器中统一处理 token 过期的情况
> 3. 示例代码：
> ```javascript
> // 请求拦截器
> const requestInterceptor = {
>   invoke(options) {
>     // 添加 token
>     const token = wx.getStorageSync('jwtToken');
>     if (token) {
>       options.header = {
>         ...options.header,
>         'Authorization': 'Bearer ' + token
>       };
>     }
>     
>     // 处理响应
>     options.success = (res) => {
>       if (res.statusCode === 401) {
>         // token 过期，重新获取
>         wx.removeStorageSync('jwtToken');
>         // 跳转到登录页面或重新获取 token
>         wx.navigateTo({ url: '/pages/login/login' });
>         return;
>       }
>       // 处理其他响应...
>     };
>   }
> };
> ```

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

