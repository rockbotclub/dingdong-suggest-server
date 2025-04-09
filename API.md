# 叮咚建议小程序后端API文档

## 1. 用户认证相关接口

### 1.1 微信登录
```java
POST /api/v1/auth/login
Request:
{
    "code": "微信登录code",
    "phoneCode": "手机号授权code"  // 可选
}
Response:
{
    "code": 200,
    "data": {
        "userInfo": {
            "nickName": "微信昵称",
            "avatarUrl": "头像URL",
            "organizationId": "组织ID",
            "organizationName": "组织名称"
        }
    }
}
```

### 1.2 发送验证码
```java
POST /api/v1/auth/send-verification-code
Request:
{
    "phone": "手机号"
}
Response:
{
    "code": 200,
    "data": {
        "success": true
    }
}
```

### 1.3 用户注册
```java
POST /api/v1/auth/register
Request:
{
    "name": "真实姓名",
    "department": "所属部门",
    "phone": "联系电话",
    "verificationCode": "验证码",
    "wxid": "微信openid"
}
Response:
{
    "code": 200,
    "data": {
        "userInfo": {
            "nickName": "微信昵称",
            "avatarUrl": "头像URL",
            "organizationId": "组织ID",
            "organizationName": "组织名称"
        }
    }
}
```

## 2. 建议管理相关接口

### 2.1 获取建议列表
```java
GET /api/v1/suggestions
Query Parameters:
- page: 页码
- pageSize: 每页数量
- status: 状态（可选，all/draft/submitted/approved/rejected/implemented）
- organizationId: 组织ID（可选，管理员可以指定组织）
Response:
{
    "code": 200,
    "data": {
        "list": [
            {
                "id": "建议ID",
                "title": "建议标题",
                "status": "状态",
                "date": "创建时间"
            }
        ],
        "total": 100
    }
}
```

### 2.2 获取建议详情
```java
GET /api/v1/suggestions/{suggestionId}
Response:
{
    "code": 200,
    "data": {
        "id": "建议ID",
        "title": "建议标题",
        "problem": "问题描述",
        "analysis": "问题分析",
        "suggestions": ["具体建议1", "具体建议2"],
        "expectedEffect": "预期效果",
        "images": ["图片URL1"],
        "submitterName": "提交人姓名",
        "submitterDepartment": "提交人部门",
        "submitterPhone": "提交人电话",
        "submitterWxid": "提交人微信ID",
        "organizationId": "组织ID",
        "organizationName": "组织名称",
        "status": "状态",
        "createTime": "创建时间",
        "updateTime": "更新时间",
        "feedbacks": []
    }
}
```

### 2.3 提交建议
```java
POST /api/v1/suggestions
Request:
{
    "title": "建议标题",
    "problem": "问题描述",
    "analysis": "问题分析",
    "suggestions": ["具体建议1", "具体建议2"],
    "expectedEffect": "预期效果",
    "images": ["图片URL1", "图片URL2"],
    "submitterName": "提交人姓名",
    "submitterDepartment": "提交人部门",
    "submitterPhone": "提交人电话",
    "submitterWxid": "提交人微信ID",
    "organizationId": "组织ID",
    "organizationName": "组织名称"
}
Response:
{
    "code": 200,
    "data": {
        "id": "建议ID"
    }
}
```

### 2.4 更新建议状态
```java
POST /api/v1/suggestions/{suggestionId}/status
Request:
{
    "status": "approved/rejected/revoked"
}
Response:
{
    "code": 200,
    "data": {
        "success": true
    }
}
```

### 2.5 提交反馈
```java
POST /api/v1/suggestions/{suggestionId}/feedback
Request:
{
    "content": "反馈内容"
}
Response:
{
    "code": 200,
    "data": {
        "success": true
    }
}
```

