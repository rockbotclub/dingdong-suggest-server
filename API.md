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
- **URL**: `/api/users/login-wx`
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
    "token": "string",
    "wxid": "string",
    "userName": "string",
    "userOrg": "string",
    "userPhone": "string",
    "status": "integer",
    "orgId": "string"
}
```
  - **Error Responses**:
    - `400 Bad Request`: 微信登录code不能为空
    - `500 Internal Server Error`: 系统异常，请稍后重试

#### 2. 发送验证码
- **URL**: `/api/users/send-verification-code`
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
    - `500 Internal Server Error`: 系统异常，请稍后重试

#### 3. 注册/验证手机号
- **URL**: `/api/users/register`
- **Method**: `POST`
- **Request Body**:
```json
{
    "phone": "string",      // 手机号码
    "verificationCode": "string", // 验证码
    "wxid": "string"      // 微信openid
}
```
- **Response**:
  - **Success (200 OK)**:
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
  - **Error Responses**:
    - `400 Bad Request`: 手机号、验证码和微信openid不能为空
    - `400 Bad Request`: 验证码错误
    - `404 Not Found`: 用户不存在
    - `500 Internal Server Error`: 系统异常，请稍后重试

### Organizations

#### 1. 创建组织
- **URL**: `/api/organizations`
- **Method**: `POST`
- **Request Body**:
```json
{
    "id": "string",
    "orgName": "string",
    "address": "string"
}
```
- **Response**: 200 OK with created organization object

#### 2. 获取组织信息
- **URL**: `/api/organizations/{id}`
- **Method**: `GET`
- **Response**: 200 OK with organization object or 404 Not Found

#### 3. 通过组织ID获取组织
- **URL**: `/api/organizations/orgid/{orgId}`
- **Method**: `GET`
- **Response**: 200 OK with organization object or 404 Not Found

#### 4. 获取所有组织
- **URL**: `/api/organizations`
- **Method**: `GET`
- **Response**: 200 OK with list of organizations

### Suggestions

#### 1. 创建建议
- **URL**: `/api/v1/suggestions`
- **Method**: `POST`
- **Request Body**:
```json
{
    "title": "string",
    "problemDescription": "string",
    "problemAnalysis": "string",
    "suggestion": "string",
    "expectedOutcome": "string",
    "images": ["string"],
    "userWxid": "string",
    "orgId": "string"
}
```
- **Response**: 200 OK with created suggestion object

#### 2. 获取建议详情
- **URL**: `/api/v1/suggestions/{id}`
- **Method**: `GET`
- **Response**: 200 OK with suggestion object

#### 3. 获取所有建议
- **URL**: `/api/v1/suggestions`
- **Method**: `GET`
- **Response**: 200 OK with list of suggestions

#### 4. 更新建议状态
- **URL**: `/api/v1/suggestions/{id}/status`
- **Method**: `PUT`
- **Request Parameters**:
  - `status`: string (新状态)
- **Response**: 200 OK

#### 5. 撤回建议
- **URL**: `/api/v1/suggestions/{id}/withdraw`
- **Method**: `PUT`
- **Response**: 200 OK

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

### SuggestionDO
```json
{
    "id": "long",
    "gmtCreate": "datetime",
    "gmtModified": "datetime",
    "title": "string",
    "problemDescription": "string",
    "problemAnalysis": "string",
    "suggestion": "string",
    "expectedOutcome": "string",
    "imageUrls": "string",
    "userWxid": "string",
    "status": "string",
    "orgId": "string"
}
```

## Error Responses

### 400 Bad Request
```json
{
    "code": "integer",
    "message": "string"
}
```

Example error codes:
- `10001`: Parameter error
- `10002`: Verification code error
- `10003`: User not found

### 401 Unauthorized
```json
{
    "code": "integer",
    "message": "Invalid or expired token"
}
```

### 403 Forbidden
```json
{
    "code": "integer",
    "message": "Access denied"
}
```

### 404 Not Found
```json
{
    "code": "integer",
    "message": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
    "code": "integer",
    "message": "System error, please try again later"
}
```

## Notes

1. All datetime fields are in ISO-8601 format
2. Phone numbers must match the pattern: ^1[3-9]\d{9}$
3. Status values: 0 = normal user, 1 = admin
4. The verification code is valid for 1 minute
5. All requests and responses use UTF-8 encoding
6. Image URLs in suggestions are stored as JSON strings
7. Suggestion status values: PENDING, APPROVED, REJECTED, WITHDRAWN

