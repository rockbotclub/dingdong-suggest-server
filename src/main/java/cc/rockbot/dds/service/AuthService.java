package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.AuthResponse;
import cc.rockbot.dds.dto.RegisterRequest;
import cc.rockbot.dds.dto.VerificationCodeRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    void sendVerificationCode(VerificationCodeRequest request);
    AuthResponse register(RegisterRequest request);
} 