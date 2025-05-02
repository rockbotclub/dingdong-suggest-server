# 叮咚建议服务 API 文档

## 基本信息

- 基础路径: `/api/v1/suggestions`
- 认证方式: JWT Token
- 响应格式: JSON
- 错误处理: 统一使用 ApiResponse 格式返回

## 通用响应格式

```json
{
    "code": 0,       // 错误码，0表示成功
    "message": "",   // 错误信息
    "data": {}       // 响应数据
}
```

## 通用错误码

| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 10000 | 系统错误 |
| 10001 | 参数错误 |
| 10002 | 资源不存在 |
| 10003 | 未授权 |
| 10004 | 禁止访问 |

## API 列表

### 1. 创建建议

创建新的建议。

**请求信息**
- 路径: `POST /api/v1/suggestions/create`
- Content-Type: `application/json`

**请求参数**
```json
{
    "jwtToken": "string",          // JWT token
    "title": "string",             // 建议标题
    "problemDescription": "string", // 问题描述
    "problemAnalysis": "string",   // 问题分析
    "suggestion": "string",        // 建议内容
    "expectedOutcome": "string",   // 预期效果
    "orgId": "string",            // 组织ID
    "images": ["string"]          // 图片URL列表（可选）
}
```

**响应参数**
```json
{
    "code": 0,
    "message": "success",
    "data": {
        "id": "long",              // 建议ID
        "title": "string",         // 建议标题
        "problemDescription": "string", // 问题描述
        "problemAnalysis": "string",   // 问题分析
        "suggestion": "string",        // 建议内容
        "expectedOutcome": "string",   // 预期效果
        "userWxid": "string",         // 用户微信ID
        "orgId": "string",            // 组织ID
        "status": "string",           // 建议状态
        "imageUrls": "string",        // 图片URL列表（JSON字符串）
        "gmtCreate": "datetime",      // 创建时间
        "gmtModified": "datetime"     // 修改时间
    }
}
```

**错误码**
| 错误码 | 说明 |
|--------|------|
| 10001 | 参数错误（建议信息为空、JWT token为空或无效） |
| 10000 | 系统错误 |

### 2. 获取建议详情

根据建议ID获取建议详情。

**请求信息**
- 路径: `GET /api/v1/suggestions/{id}`
- 参数:
  - id: 建议ID（路径参数）
  - jwtToken: JWT token（查询参数）

**响应参数**
```json
{
    "code": 0,
    "message": "success",
    "data": {
        "id": "long",              // 建议ID
        "title": "string",         // 建议标题
        "problemDescription": "string", // 问题描述
        "problemAnalysis": "string",   // 问题分析
        "suggestion": "string",        // 建议内容
        "expectedOutcome": "string",   // 预期效果
        "userWxid": "string",         // 用户微信ID
        "orgId": "string",            // 组织ID
        "status": "string",           // 建议状态
        "imageUrls": "string",        // 图片URL列表（JSON字符串）
        "gmtCreate": "datetime",      // 创建时间
        "gmtModified": "datetime"     // 修改时间
    }
}
```

**错误码**
| 错误码 | 说明 |
|--------|------|
| 10001 | 参数错误（建议ID为空、JWT token无效） |
| 23000 | 建议不存在 |
| 10004 | 无权查看其他人的建议 |
| 10000 | 系统错误 |

### 3. 获取建议列表

获取用户的所有建议列表，支持分页。

**请求信息**
- 路径: `GET /api/v1/suggestions`
- 参数:
  - jwtToken: JWT token
  - orgId: 组织ID
  - year: 年份
  - page: 页码（默认0）
  - size: 每页大小（默认10）
  - sort: 排序字段（默认"gmtCreate,desc"）

**响应参数**
```json
{
    "code": 0,
    "message": "success",
    "data": {
        "content": [
            {
                "id": "long",          // 建议ID
                "title": "string",     // 建议标题
                "status": "string",    // 建议状态
                "createTime": "datetime" // 创建时间
            }
        ],
        "pageable": {
            "pageNumber": "int",       // 当前页码
            "pageSize": "int",         // 每页大小
            "sort": {                  // 排序信息
                "sorted": "boolean",
                "unsorted": "boolean",
                "empty": "boolean"
            },
            "offset": "long",
            "paged": "boolean",
            "unpaged": "boolean"
        },
        "totalElements": "long",       // 总记录数
        "totalPages": "int",          // 总页数
        "last": "boolean",            // 是否最后一页
        "size": "int",                // 每页大小
        "number": "int",              // 当前页码
        "sort": {                     // 排序信息
            "sorted": "boolean",
            "unsorted": "boolean",
            "empty": "boolean"
        },
        "numberOfElements": "int",     // 当前页记录数
        "first": "boolean",           // 是否第一页
        "empty": "boolean"            // 是否为空
    }
}
```

**错误码**
| 错误码 | 说明 |
|--------|------|
| 10001 | 参数错误（JWT token无效） |
| 10000 | 系统错误 |

### 4. 撤回建议

撤回已提交的建议。

**请求信息**
- 路径: `POST /api/v1/suggestions/withdraw/{id}`
- 参数:
  - id: 建议ID（路径参数）
  - jwtToken: JWT token（查询参数）

**响应参数**
```json
{
    "code": 0,
    "message": "success",
    "data": null
}
```

**错误码**
| 错误码 | 说明 |
|--------|------|
| 10001 | 参数错误（建议ID为空、JWT token无效） |
| 23000 | 建议不存在 |
| 10004 | 无权撤回其他人的建议 |
| 23001 | 建议状态无效（只有未审批状态的建议才能撤回） |
| 10000 | 系统错误 |

### 5. 删除建议

删除已撤回的建议。

**请求信息**
- 路径: `POST /api/v1/suggestions/delete/{id}`
- 参数:
  - id: 建议ID（路径参数）
  - jwtToken: JWT token（查询参数）

**响应参数**
```json
{
    "code": 0,
    "message": "success",
    "data": null
}
```

**错误码**
| 错误码 | 说明 |
|--------|------|
| 10001 | 参数错误（建议ID为空、JWT token无效） |
| 23000 | 建议不存在 |
| 10004 | 无权删除其他人的建议 |
| 23001 | 建议状态无效（只有已撤回的建议才能删除） |
| 10000 | 系统错误 |

## 建议状态说明

| 状态 | 说明 |
|------|------|
| SUBMITTED | 已提交（未审批） |
| WITHDRAWN | 已撤回 |

## 注意事项

1. 所有接口都需要在请求中携带有效的 JWT token
2. 图片上传需要先调用文件上传接口获取图片URL
3. 分页参数从0开始计数
4. 排序参数格式为：字段名,排序方向（asc/desc）
5. 时间格式统一使用 ISO 8601 标准

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

