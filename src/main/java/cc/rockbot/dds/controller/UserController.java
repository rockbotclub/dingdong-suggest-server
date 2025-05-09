package cc.rockbot.dds.controller;

import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cc.rockbot.dds.service.SmsService;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.dto.UserLoginRequest;
import cc.rockbot.dds.dto.SendVerificationRequest;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import cc.rockbot.dds.model.OrganizationDO;
import cc.rockbot.dds.service.OrganizationService;
import java.util.List;
import java.util.stream.Collectors;
import cc.rockbot.dds.dto.UserRegisterDTO;
import cc.rockbot.dds.dto.ApiResponse;
import cc.rockbot.dds.dto.UserRegisterRequest;
import cc.rockbot.dds.util.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import cc.rockbot.dds.dto.OrganizationDTO;
import java.util.ArrayList;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关的接口")
public class UserController extends BaseController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/login-by-wx-code")
    @Operation(summary = "微信登录", description = "使用微信code进行登录")
    public ApiResponse<UserRegisterDTO> login(@RequestBody UserLoginRequest request) {
        commonApiResponses();
        try {
            if (request == null || request.getWxCode() == null || request.getWxCode().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "微信登录code不能为空");
            }
            
            UserRegisterDTO user = userService.loginByWxCode(request.getWxCode());
            return ApiResponse.success(user);
        } catch (BusinessException e) {
            log.warn("微信登录业务异常: {}", e.getMessage(), e);
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("微信登录系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @PostMapping("/send-verification-code")
    @Operation(summary = "发送验证码", description = "发送手机验证码")
    public ApiResponse<Void> sendVerificationCode(@RequestBody SendVerificationRequest request) {
        commonApiResponses();
        try {
            if (request == null || request.getPhone() == null || request.getPhone().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "手机号不能为空");
            }
            
            userService.sendVerificationCode(request.getPhone());
            return ApiResponse.success(null);
        } catch (BusinessException e) {
            log.warn("发送验证码业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("发送验证码系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @PostMapping("/register-by-phone-and-code")
    @Operation(summary = "手机号注册", description = "使用手机号和验证码进行注册")
    public ApiResponse<UserRegisterDTO> register(@RequestBody UserRegisterRequest request) {
        commonApiResponses();
        try {
            if (request == null || request.getPhone() == null || request.getPhone().isEmpty() 
                    || request.getVerificationCode() == null || request.getVerificationCode().isEmpty()
                    || request.getWxCode() == null || request.getWxCode().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "手机号、验证码和微信临时code不能为空");
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

            // 3. 保存用户并生成JWT token
            UserRegisterDTO userRegisterDTO = userService.register(userDO, request.getWxCode());
            return ApiResponse.success(userRegisterDTO);
        } catch (BusinessException e) {
            log.warn("用户注册业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("用户注册系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @GetMapping("/user-org")
    @Operation(summary = "获取用户组织", description = "根据JWT token获取用户所属的组织")
    public ApiResponse<List<OrganizationDTO>> getOrganizationsByUserWxid(@RequestParam String jwtToken) {
        commonApiResponses();
        try {
            // 从JWT token中获取用户信息
            log.info("获取用户组织，JWT token: {}", jwtToken);
            String wxid = jwtTokenService.getWxidFromToken(jwtToken);
            if (wxid == null) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }

            List<OrganizationDTO> organizationDTOs = new ArrayList<>();
            List<UserDO> userDOs = userRepository.findByWxid(wxid);
            for (UserDO userDO : userDOs) {
                Optional<OrganizationDO> organizationDO = organizationService.getOrganizationById(userDO.getOrgId());
                if (organizationDO.isPresent()) {
                    OrganizationDTO organizationDTO = new OrganizationDTO();
                    organizationDTO.setId(organizationDO.get().getId());
                    organizationDTO.setOrgName(organizationDO.get().getOrgName());
                    organizationDTO.setUserName(userDO.getUserName());
                    organizationDTO.setUserPhone(userDO.getUserPhone());
                    organizationDTOs.add(organizationDTO);
                }
            }
            return ApiResponse.success(organizationDTOs);
        } catch (BusinessException e) {
            log.warn("获取用户组织业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取用户组织系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }
} 