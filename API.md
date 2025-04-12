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
  - **Success (200 OK)**:
```json
{
    "user": {
        "id": "long",
        "wxid": "string",
        "userName": "string",
        "userOrg": "string",
        "userPhone": "string",
        "status": "integer",
        "orgId": "string",
        "gmtCreate": "string",
        "gmtModified": "string"
    },
    "token": "string"
}
```
  - **Error Responses**:
    - `400 Bad Request`: 微信登录code不能为空
    - `400 Bad Request`: 微信登录失败 (invalid code)
    - `404 Not Found`: 未找到openid
    - `400 Bad Request`: 新用户需要先注册

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
  - **Success (200 OK)**: 空响应
  - **Error Responses**:
    - `400 Bad Request`: 手机号不能为空
    - `404 Not Found`: 用户不存在，请联系管理员后台添加
    - `500 Internal Server Error`: 发送验证码失败

#### 3. 注册/验证手机号
- **URL**: `/auth/register`
- **Method**: `POST`
- **Request Body**:
```json
{
    "phone": "string",      // 手机号码
    "verificationCode": "string", // 验证码
    "wxCode": "string"      // 微信登录code
}
```
- **Response**:
  - **Success (200 OK)**: 空响应
  - **Error Responses**:
    - `400 Bad Request`: 手机号、验证码和微信code不能为空
    - `400 Bad Request`: 验证码错误
    - `400 Bad Request`: 微信登录失败
    - `404 Not Found`: 未找到openid
    - `400 Bad Request`: 用户已存在
    - `500 Internal Server Error`: 用户注册异常

### Users

#### 1. 创建用户
- **URL**: `/users`
- **Method**: `POST`
- **Request Body**:
```json
{
    "wxid": "string",
    "userName": "string",
    "userOrg": "string",
    "userPhone": "string",
    "status": "integer",
    "orgId": "string"
}
```
- **Response**: 200 OK with created user object

#### 2. 获取用户信息
- **URL**: `/users/{id}`
- **Method**: `GET`
- **Response**: 200 OK with user object or 404 Not Found

#### 3. 通过微信ID获取用户
- **URL**: `/users/wxid/{userWxid}`
- **Method**: `GET`
- **Response**: 200 OK with user object or 404 Not Found

#### 4. 获取组织下的所有用户
- **URL**: `/users/org/{orgId}`
- **Method**: `GET`
- **Response**: 200 OK with list of users

#### 5. 获取所有用户
- **URL**: `/users`
- **Method**: `GET`
- **Response**: 200 OK with list of users

#### 6. 更新用户信息
- **URL**: `/users/{id}`
- **Method**: `PUT`
- **Request Body**: User object with updated fields
- **Response**: 200 OK with updated user object

#### 7. 删除用户
- **URL**: `/users/{id}`
- **Method**: `DELETE`
- **Response**: 200 OK

### Organizations

#### 1. 创建组织
- **URL**: `/organizations`
- **Method**: `POST`
- **Request Body**: Organization object
- **Response**: 200 OK with created organization object

#### 2. 获取组织信息
- **URL**: `/organizations/{id}`
- **Method**: `GET`
- **Response**: 200 OK with organization object or 404 Not Found

#### 3. 通过组织ID获取组织
- **URL**: `/organizations/orgid/{orgId}`
- **Method**: `GET`
- **Response**: 200 OK with organization object or 404 Not Found

#### 4. 获取所有组织
- **URL**: `/organizations`
- **Method**: `GET`
- **Response**: 200 OK with list of organizations

#### 5. 更新组织信息
- **URL**: `/organizations/{id}`
- **Method**: `PUT`
- **Request Body**: Organization object with updated fields
- **Response**: 200 OK with updated organization object

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

