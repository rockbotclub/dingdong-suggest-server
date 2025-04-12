package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.UserVO;
import cc.rockbot.dds.dto.VerificationCodeRequest;
import cc.rockbot.dds.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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
    public ResponseEntity<?> sendVerificationCode(@RequestBody VerificationCodeRequest request) {
        authService.sendVerificationCode(request.getPhone());
        return ResponseEntity.ok().build();
    }

} 