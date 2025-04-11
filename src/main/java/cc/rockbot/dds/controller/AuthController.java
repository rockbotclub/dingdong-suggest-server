package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.AuthResponse;
import cc.rockbot.dds.dto.RegisterRequest;
import cc.rockbot.dds.dto.VerificationCodeRequest;
import cc.rockbot.dds.dto.UpdateUserInfoRequest;
import cc.rockbot.dds.dto.WxLoginRequest;
import cc.rockbot.dds.dto.WxLoginResponse;
import cc.rockbot.dds.service.AuthService;
import cc.rockbot.dds.service.WxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final WxService wxService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody VerificationCodeRequest request) {
        authService.sendVerificationCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login-wx")
    public ResponseEntity<WxLoginResponse> loginWx(@Valid @RequestBody WxLoginRequest request) {
        return ResponseEntity.ok(wxService.login(request.getCode()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String token) {
        String wxid = token.substring(7); // 去掉 "Bearer " 前缀
        return ResponseEntity.ok(wxService.refreshToken(wxid));
    }

    @PostMapping("/update-user-info")
    public ResponseEntity<WxLoginResponse> updateUserInfo(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateUserInfoRequest request) {
        String wxid = token.substring(7); // 去掉 "Bearer " 前缀
        return ResponseEntity.ok(wxService.updateUserInfo(wxid, request));
    }
} 