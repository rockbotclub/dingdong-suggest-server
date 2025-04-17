package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.UserVO;
import cc.rockbot.dds.dto.UserRegisterRequest;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.service.impl.AuthServiceImpl;
import cc.rockbot.dds.service.impl.SmsServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SmsServiceImpl smsService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 设置微信配置
        ReflectionTestUtils.setField(authService, "appid", "test_appid");
        ReflectionTestUtils.setField(authService, "secret", "test_secret");
    }

    @Test
    void login_ValidWxCode_ReturnsUserVO() {
        // Given
        String wxCode = "valid_wx_code";
        String openid = "test_openid";
        UserDO mockUser = new UserDO();
        mockUser.setId(1L);
        mockUser.setWxid(openid);
        mockUser.setUserName("Test User");

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("openid", openid);
        mockResponse.put("session_key", "test_session_key");

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse.toJSONString());
        when(userRepository.findByWxid(openid)).thenReturn(mockUser);

        // When
        UserVO result = authService.login(wxCode);

        // Then
        assertNotNull(result);
        assertEquals(mockUser.getId(), result.getUser().getId());
        assertEquals(mockUser.getWxid(), result.getUser().getWxid());
        assertEquals(mockUser.getUserName(), result.getUser().getUserName());
        assertNotNull(result.getToken());

        // Verify
        verify(restTemplate).getForObject(anyString(), any());
        verify(userRepository).findByWxid(openid);
    }

    @Test
    void login_EmptyWxCode_ThrowsBusinessException() {
        // Given
        String wxCode = "";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(wxCode);
        });

        assertEquals(ErrorCode.PARAM_ERROR.getCode(), exception.getErrorCode().getCode());
        assertEquals("微信登录code不能为空", exception.getDetailMessage());
    }

    @Test
    void login_InvalidWxCode_ThrowsBusinessException() {
        // Given
        String wxCode = "invalid_wx_code";
        JSONObject mockResponse = new JSONObject();
        mockResponse.put("errcode", 40029);
        mockResponse.put("errmsg", "invalid code");

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse.toJSONString());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(wxCode);
        });

        assertEquals(ErrorCode.WX_LOGIN_FAILED.getCode(), exception.getErrorCode().getCode());
        assertEquals("invalid code", exception.getDetailMessage());
    }

    @Test
    void login_NoOpenid_ThrowsBusinessException() {
        // Given
        String wxCode = "valid_wx_code";
        JSONObject mockResponse = new JSONObject();
        mockResponse.put("session_key", "test_session_key");

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse.toJSONString());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(wxCode);
        });

        assertEquals(ErrorCode.WX_OPENID_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void login_NewUser_ThrowsBusinessException() {
        // Given
        String wxCode = "valid_wx_code";
        String openid = "new_user_openid";

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("openid", openid);
        mockResponse.put("session_key", "test_session_key");

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse.toJSONString());
        when(userRepository.findByWxid(openid)).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(wxCode);
        });

        assertEquals(ErrorCode.NEW_USER_NEED_REGISTER.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void register_ValidRequest_ReturnsTrue() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();
        request.setPhone("13800138000");
        request.setVerificationCode("123456");
        request.setWxid("test_wxid");

        when(smsService.verifyCode(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByUserPhone(anyString())).thenReturn(new UserDO());

        // When
        boolean result = authService.register(request);

        // Then
        assertTrue(result);

        // Verify
        verify(smsService).verifyCode(request.getPhone(), request.getVerificationCode());
        verify(userRepository).findByUserPhone(request.getPhone());
        verify(userRepository).save(any(UserDO.class));
    }

    @Test
    void register_InvalidRequest_ThrowsBusinessException() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();
        request.setPhone("13800138000");
        request.setVerificationCode("123456");
        request.setWxid("test_wxid");

        when(smsService.verifyCode(anyString(), anyString())).thenReturn(false);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(request);
        });

        assertEquals(ErrorCode.VERIFICATION_CODE_ERROR.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void register_EmptyRequest_ThrowsBusinessException() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();
        request.setPhone("");
        request.setVerificationCode("");
        request.setWxid("");

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(request);
        });

        assertEquals("手机号、验证码和微信openid不能为空", exception.getDetailMessage());
    }

    @Test
    void register_InvalidVerificationCode_ThrowsBusinessException() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();
        request.setPhone("13800138000");
        request.setVerificationCode("123456");
        request.setWxid("test_wxid");

        when(smsService.verifyCode(anyString(), anyString())).thenReturn(false);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(request);
        });

        assertEquals(ErrorCode.VERIFICATION_CODE_ERROR.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void sendVerificationCode_ValidPhone_ReturnsTrue() {
        // Given
        String phoneNumber = "13800138000";
        UserDO mockUser = new UserDO();
        mockUser.setUserPhone(phoneNumber);

        when(userRepository.findByUserPhone(phoneNumber)).thenReturn(mockUser);
        when(smsService.sendVerificationCode(phoneNumber)).thenReturn(true);

        // When
        boolean result = authService.sendVerificationCode(phoneNumber);

        // Then
        assertTrue(result);

        // Verify
        verify(userRepository).findByUserPhone(phoneNumber);
        verify(smsService).sendVerificationCode(phoneNumber);
    }

    @Test
    void sendVerificationCode_EmptyPhone_ThrowsBusinessException() {
        // Given
        String phoneNumber = "";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.sendVerificationCode(phoneNumber);
        });

        assertEquals(ErrorCode.PARAM_ERROR.getCode(), exception.getErrorCode().getCode());
        assertEquals("手机号不能为空", exception.getDetailMessage());
    }

    @Test
    void sendVerificationCode_UserNotFound_ThrowsBusinessException() {
        // Given
        String phoneNumber = "13800138000";

        when(userRepository.findByUserPhone(phoneNumber)).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.sendVerificationCode(phoneNumber);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void sendVerificationCode_SendFailed_ThrowsBusinessException() {
        // Given
        String phoneNumber = "13800138000";
        UserDO mockUser = new UserDO();
        mockUser.setUserPhone(phoneNumber);

        when(userRepository.findByUserPhone(phoneNumber)).thenReturn(mockUser);
        when(smsService.sendVerificationCode(phoneNumber)).thenReturn(false);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.sendVerificationCode(phoneNumber);
        });

        assertEquals(ErrorCode.SYSTEM_ERROR.getCode(), exception.getErrorCode().getCode());
        assertEquals("发送验证码失败", exception.getDetailMessage());
    }
} 