package cc.rockbot.dds.controller;

import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cc.rockbot.dds.service.SmsService;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.UserLoginRequest;
import cc.rockbot.dds.dto.SendVerificationRequest;
import cc.rockbot.dds.dto.UserVO;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import cc.rockbot.dds.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;


@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/login-wx")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            if (request == null || request.getCode() == null || request.getCode().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "微信登录code不能为空");
            }
            
            UserVO user = userService.login(request.getCode());
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
            
            userService.sendVerificationCode(request.getPhone());
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
    public ResponseEntity<?> register(@RequestBody UserLoginRequest request) {
        try {
            if (request == null || request.getPhone() == null || request.getPhone().isEmpty() 
                    || request.getVerificationCode() == null || request.getVerificationCode().isEmpty()
                    || request.getWxid() == null || request.getWxid().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "手机号、验证码和微信openid不能为空");
            }
            
            // 1. 验证手机号和验证码是否一致
            boolean isCodeValid = smsService.verifyCode(request.getPhone(), request.getVerificationCode());
            if (!isCodeValid) {
                throw new BusinessException(ErrorCode.VERIFICATION_CODE_ERROR, "验证码错误");
            }

            // 2. 检查用户是否已存在
            UserDO userDO = userRepository.findByUserPhone(request.getPhone());
            if (userDO == null) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }

            userDO.setWxid(request.getWxid());
            return ResponseEntity.ok(userRepository.save(userDO));
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