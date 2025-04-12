package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.AuthResponse;
import cc.rockbot.dds.dto.VerificationCodeRequest;

public interface AuthService {
    /**
     * 微信登录，验证code是否有效
     * 查询数据库中是否存在该用户
     * 如果存在，则返回用户信息
     * 如果不存在，则返回null
     * @param request
     * @return
     */
    AuthResponse login(AuthRequest request);

    void sendVerificationCode(VerificationCodeRequest request);
} 