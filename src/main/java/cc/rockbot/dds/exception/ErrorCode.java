package cc.rockbot.dds.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 通用错误码
    SUCCESS(0, "成功"),
    SYSTEM_ERROR(10000, "系统错误"),
    PARAM_ERROR(10001, "参数错误"),
    NOT_FOUND(10002, "资源不存在"),
    UNAUTHORIZED(10003, "未授权"),
    FORBIDDEN(10004, "禁止访问"),
    
    // 认证相关错误码 (20000-20999)
    WX_LOGIN_FAILED(20000, "微信登录失败"),
    WX_CODE_INVALID(20001, "微信登录code无效"),
    WX_OPENID_NOT_FOUND(20002, "未获取到openid"),
    NEW_USER_NEED_REGISTER(20003, "新用户登录，请先验证手机号"),
    VERIFICATION_CODE_ERROR(20004, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(20005, "验证码已过期"),
    PHONE_NUMBER_INVALID(20006, "手机号格式不正确"),
    USER_NOT_FOUND(20007, "用户不存在"),
    
    // 用户相关错误码 (21000-21999)
    USER_STATUS_INVALID(21001, "用户状态无效"),
    
    // 组织相关错误码 (22000-22999)
    ORG_NOT_FOUND(22000, "组织不存在"),
    ORG_ALREADY_EXISTS(22001, "组织已存在"),
    
    // 建议相关错误码 (23000-23999)
    SUGGESTION_NOT_FOUND(23000, "建议不存在"),
    SUGGESTION_STATUS_INVALID(23001, "建议状态无效"),

    // 管理员相关错误码 (24000-24999)
    ADMIN_NOT_FOUND(24000, "管理员不存在"),
    ADMIN_ALREADY_EXISTS(24001, "管理员已存在");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
} 