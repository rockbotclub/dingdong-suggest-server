# API Documentation

## Base URL
```
https://springboot-wu96-152263-4-1352937363.sh.run.tcloudbase.com/api
```

## Authentication
All authenticated endpoints require a Bearer token in the Authorization header:
```
Authorization: Bearer your-token-here
```

## API Endpoints

### Authentication

#### 1. 微信登录
- **URL**: `/auth/login-wx`
- **Method**: `POST`
- **Request Body**:
```json
{
    "code": "string" // 微信登录code
}
```
- **Response**:
```json
{
    "token": "string",
    "wxid": "string",
    "userName": "string",
    "userOrg": "string",
    "userPhone": "string",
    "status": 0,
    "orgId": "string"
}
```

#### 2. 发送验证码
- **URL**: `/auth/send-verification-code`
- **Method**: `POST`
- **Request Body**:
```json
{
    "phone": "string" // 手机号码
}
```
- **Response**:
```json
{
    "success": true,
    "message": "string"
}
```

#### 3. 验证验证码
- **URL**: `/auth/verify-code`
- **Method**: `POST`
- **Request Body**:
```json
{
    "phone": "string", // 手机号码
    "verificationCode": "string", // 验证码
    "wxCode": "string" // 微信登录code
}
```
- **Response**:
```json
{
    "success": true,
    "message": "string",
    "token": "string", // 验证成功时返回token
    "user": {
        "id": "long",
        "wxid": "string",
        "userName": "string",
        "userOrg": "string",
        "userPhone": "string",
        "status": "integer",
        "orgId": "string"
    }
}
```

#### 4. 刷新Token
- **URL**: `/auth/refresh-token`
- **Method**: `POST`
- **Headers**:
  - `Authorization: Bearer your-token`
- **Response**: `string` (new token)

#### 5. 更新用户信息
- **URL**: `/auth/update-user-info`
- **Method**: `POST`
- **Headers**:
  - `Authorization: Bearer your-token`
- **Request Body**:
```json
{
    "userName": "string",
    "userOrg": "string",
    "userPhone": "string",
    "orgId": "string"
}
```
- **Response**:
```json
{
    "token": "string",
    "wxid": "string",
    "userName": "string",
    "userOrg": "string",
    "userPhone": "string",
    "status": 0,
    "orgId": "string"
}
```

### Suggestions

#### 1. 创建建议
- **URL**: `/v1/suggestions`
- **Method**: `POST`
- **Headers**:
  - `Authorization: Bearer your-token`
- **Request Body**: `SuggestionRequest`
- **Response**: `SuggestionResponse`

#### 2. 获取建议详情
- **URL**: `/v1/suggestions/{id}`
- **Method**: `GET`
- **Headers**:
  - `Authorization: Bearer your-token`
- **Response**: `SuggestionResponse`

#### 3. 更新建议
- **URL**: `/v1/suggestions/{id}`
- **Method**: `PUT`
- **Headers**:
  - `Authorization: Bearer your-token`
- **Request Body**: `SuggestionRequest`
- **Response**: `void`

#### 4. 删除建议
- **URL**: `/v1/suggestions/{id}`
- **Method**: `DELETE`
- **Headers**:
  - `Authorization: Bearer your-token`
- **Response**: `void`

#### 5. 获取所有建议
- **URL**: `/v1/suggestions`
- **Method**: `GET`
- **Headers**:
  - `Authorization: Bearer your-token`
- **Response**: `List<SuggestionResponse>`

### Organizations

#### 1. 创建组织
- **URL**: `/organizations`
- **Method**: `POST`
- **Request Body**: `OrganizationDO`
- **Response**: `OrganizationDO`

#### 2. 获取组织详情
- **URL**: `/organizations/{id}`
- **Method**: `GET`
- **Response**: `OrganizationDO`

#### 3. 获取所有组织
- **URL**: `/organizations`
- **Method**: `GET`
- **Response**: `List<OrganizationDO>`

#### 4. 更新组织
- **URL**: `/organizations/{id}`
- **Method**: `PUT`
- **Request Body**: `OrganizationDO`
- **Response**: `OrganizationDO`

#### 5. 删除组织
- **URL**: `/organizations/{id}`
- **Method**: `DELETE`
- **Response**: `void`

### Users

#### 1. 创建用户
- **URL**: `/users`
- **Method**: `POST`
- **Request Body**: `UserDO`
- **Response**: `UserDO`

#### 2. 获取用户详情
- **URL**: `/users/{id}`
- **Method**: `GET`
- **Response**: `UserDO`

#### 3. 根据微信ID获取用户
- **URL**: `/users/wxid/{userWxid}`
- **Method**: `GET`
- **Response**: `UserDO`

#### 4. 获取组织下的所有用户
- **URL**: `/users/org/{orgId}`
- **Method**: `GET`
- **Response**: `List<UserDO>`

#### 5. 获取所有用户
- **URL**: `/users`
- **Method**: `GET`
- **Response**: `List<UserDO>`

#### 6. 更新用户
- **URL**: `/users/{id}`
- **Method**: `PUT`
- **Request Body**: `UserDO`
- **Response**: `UserDO`

#### 7. 删除用户
- **URL**: `/users/{id}`
- **Method**: `DELETE`
- **Response**: `void`

## Data Models

### OrganizationDO
```json
{
    "id": "string",
    "gmtCreate": "datetime",
    "gmtModified": "datetime",
    "orgName": "string",
    "address": "string"
}
```

### UserDO
```json
{
    "id": "long",
    "gmtCreate": "datetime",
    "gmtModified": "datetime",
    "wxid": "string",
    "userName": "string",
    "userOrg": "string",
    "userPhone": "string",
    "status": "integer",
    "orgId": "string"
}
```

## Error Responses

### 400 Bad Request
```json
{
    "error": "string",
    "message": "string"
}
```

### 401 Unauthorized
```json
{
    "error": "Unauthorized",
    "message": "Invalid or expired token"
}
```

### 403 Forbidden
```json
{
    "error": "Forbidden",
    "message": "Access denied"
}
```

### 404 Not Found
```json
{
    "error": "Not Found",
    "message": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
    "error": "Internal Server Error",
    "message": "An unexpected error occurred"
}
```

