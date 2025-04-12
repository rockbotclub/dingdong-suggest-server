package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.UserVO;
import cc.rockbot.dds.dto.SendVerificationRequest;
import cc.rockbot.dds.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import cc.rockbot.dds.dto.UserRegisterRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login-wx")
    public ResponseEntity<UserVO> login(@RequestBody AuthRequest request) {
        try {
            UserVO user = authService.login(request.getCode());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody SendVerificationRequest request) {
        authService.sendVerificationCode(request.getPhone());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

} 