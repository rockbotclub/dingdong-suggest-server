package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.UserVO;
import cc.rockbot.dds.dto.VerificationCodeRequest;
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
    void login_InvalidWxCode_ReturnsNull() {
        // Given
        String wxCode = "invalid_wx_code";
        JSONObject mockResponse = new JSONObject();
        mockResponse.put("errcode", 40029);
        mockResponse.put("errmsg", "invalid code");

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse.toJSONString());

        // When
        UserVO result = authService.login(wxCode);

        // Then
        assertNull(result);

        // Verify
        verify(restTemplate).getForObject(anyString(), any());
        verify(userRepository, never()).findByWxid(anyString());
    }

    @Test
    void login_NewUser_ReturnsNull() {
        // Given
        String wxCode = "valid_wx_code";
        String openid = "new_user_openid";

        JSONObject mockResponse = new JSONObject();
        mockResponse.put("openid", openid);
        mockResponse.put("session_key", "test_session_key");

        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponse.toJSONString());
        when(userRepository.findByWxid(openid)).thenReturn(null);

        // When
        UserVO result = authService.login(wxCode);

        // Then
        assertNull(result);

        // Verify
        verify(restTemplate).getForObject(anyString(), any());
        verify(userRepository).findByWxid(openid);
    }

    @Test
    void verifyVerificationCode_ValidCode_ReturnsTrue() {
        // Given
        VerificationCodeRequest request = new VerificationCodeRequest();
        request.setPhone("13800138000");
        request.setVerificationCode("123456");
        request.setWxCode("valid_wx_code");

        when(smsService.verifyCode(anyString(), anyString())).thenReturn(true);

        // When
        boolean result = authService.verifyVerificationCode(request);

        // Then
        assertTrue(result);

        // Verify
        verify(smsService).verifyCode(request.getPhone(), request.getVerificationCode());
    }

    @Test
    void verifyVerificationCode_InvalidCode_ReturnsFalse() {
        // Given
        VerificationCodeRequest request = new VerificationCodeRequest();
        request.setPhone("13800138000");
        request.setVerificationCode("wrong_code");
        request.setWxCode("valid_wx_code");

        when(smsService.verifyCode(anyString(), anyString())).thenReturn(false);

        // When
        boolean result = authService.verifyVerificationCode(request);

        // Then
        assertFalse(result);

        // Verify
        verify(smsService).verifyCode(request.getPhone(), request.getVerificationCode());
    }

    @Test
    void sendVerificationCode_ValidPhone_ReturnsTrue() {
        // Given
        String phoneNumber = "13800138000";
        when(smsService.sendVerificationCode(phoneNumber)).thenReturn(true);

        // When
        boolean result = authService.sendVerificationCode(phoneNumber);

        // Then
        assertTrue(result);

        // Verify
        verify(smsService).sendVerificationCode(phoneNumber);
    }

    @Test
    void sendVerificationCode_InvalidPhone_ReturnsFalse() {
        // Given
        String phoneNumber = "invalid_phone";
        when(smsService.sendVerificationCode(phoneNumber)).thenReturn(false);

        // When
        boolean result = authService.sendVerificationCode(phoneNumber);

        // Then
        assertFalse(result);

        // Verify
        verify(smsService).sendVerificationCode(phoneNumber);
    }
} 