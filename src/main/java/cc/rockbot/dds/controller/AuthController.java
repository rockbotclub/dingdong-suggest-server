package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.UserVO;
import cc.rockbot.dds.dto.SendVerificationRequest;
import cc.rockbot.dds.service.AuthService;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import cc.rockbot.dds.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import cc.rockbot.dds.dto.UserRegisterRequest;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login-wx")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            if (request == null || request.getCode() == null || request.getCode().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "微信登录code不能为空");
            }
            
            UserVO user = authService.login(request.getCode());
            return ResponseEntity.ok(user);
        } catch (BusinessException e) {
            log.warn("微信登录业务异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getErrorCode().getCode(), e.getDetailMessage()));
        } catch (Exception e) {
            log.error("微信登录系统异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试"));
        }
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody SendVerificationRequest request) {
        try {
            if (request == null || request.getPhone() == null || request.getPhone().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "手机号不能为空");
            }
            
            authService.sendVerificationCode(request.getPhone());
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            log.warn("发送验证码业务异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getErrorCode().getCode(), e.getDetailMessage()));
        } catch (Exception e) {
            log.error("发送验证码系统异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        try {
            if (request == null || request.getPhone() == null || request.getPhone().isEmpty() 
                    || request.getVerificationCode() == null || request.getVerificationCode().isEmpty()
                    || request.getWxCode() == null || request.getWxCode().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "请求参数不能为空");
            }
            
            authService.register(request);
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            log.warn("用户注册业务异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getErrorCode().getCode(), e.getDetailMessage()));
        } catch (Exception e) {
            log.error("用户注册系统异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试"));
        }
    }
} 