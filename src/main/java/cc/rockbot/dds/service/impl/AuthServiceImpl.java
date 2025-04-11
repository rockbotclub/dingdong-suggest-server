package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.AuthResponse;
import cc.rockbot.dds.dto.RegisterRequest;
import cc.rockbot.dds.dto.VerificationCodeRequest;
import cc.rockbot.dds.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse login(AuthRequest request) {
        // TODO: Implement WeChat login logic
        AuthResponse response = new AuthResponse();
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setNickName("Test User");
        userInfo.setAvatarUrl("https://example.com/avatar.jpg");
        userInfo.setOrganizationId("org123");
        userInfo.setOrganizationName("Test Organization");
        response.setUserInfo(userInfo);
        return response;
    }

    @Override
    public void sendVerificationCode(VerificationCodeRequest request) {
        // TODO: Implement SMS verification code sending logic
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // TODO: Implement user registration logic
        AuthResponse response = new AuthResponse();
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setNickName(request.getName());
        userInfo.setAvatarUrl("https://example.com/avatar.jpg");
        userInfo.setOrganizationId("org123");
        userInfo.setOrganizationName("Test Organization");
        response.setUserInfo(userInfo);
        return response;
    }
} 