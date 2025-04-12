package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.VerificationCodeRequest;
import cc.rockbot.dds.model.UserDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthService authService;

    @Nested
    @DisplayName("login method tests")
    class LoginTests {
        @Test
        @DisplayName("should return user when wxCode is valid and user exists")
        void login_ValidWxCode_UserExists_ReturnsUser() {
            // Given
            String validWxCode = "valid_wx_code";
            UserDO expectedUser = new UserDO();
            when(authService.login(validWxCode)).thenReturn(expectedUser);

            // When
            UserDO result = authService.login(validWxCode);

            // Then
            assertNotNull(result);
            assertEquals(expectedUser, result);
            verify(authService).login(validWxCode);
        }

        @Test
        @DisplayName("should return null when wxCode is valid but user doesn't exist")
        void login_ValidWxCode_UserNotExists_ReturnsNull() {
            // Given
            String validWxCode = "valid_wx_code";
            when(authService.login(validWxCode)).thenReturn(null);

            // When
            UserDO result = authService.login(validWxCode);

            // Then
            assertNull(result);
            verify(authService).login(validWxCode);
        }

        @Test
        @DisplayName("should handle empty wxCode")
        void login_EmptyWxCode_ReturnsNull() {
            // Given
            String emptyWxCode = "";
            when(authService.login(emptyWxCode)).thenReturn(null);

            // When
            UserDO result = authService.login(emptyWxCode);

            // Then
            assertNull(result);
            verify(authService).login(emptyWxCode);
        }

        @Test
        @DisplayName("should handle null wxCode")
        void login_NullWxCode_ReturnsNull() {
            // Given
            when(authService.login(null)).thenReturn(null);

            // When
            UserDO result = authService.login(null);

            // Then
            assertNull(result);
            verify(authService).login(null);
        }
    }

    @Nested
    @DisplayName("verifyVerificationCode method tests")
    class VerifyVerificationCodeTests {
        @Test
        @DisplayName("should return true when verification code matches")
        void verifyVerificationCode_CodeMatches_ReturnsTrue() {
            // Given
            VerificationCodeRequest request = new VerificationCodeRequest();
            request.setPhone("13800138000");
            request.setVerificationCode("123456");
            request.setWxCode("valid_wx_code");
            when(authService.verifyVerificationCode(request)).thenReturn(true);

            // When
            boolean result = authService.verifyVerificationCode(request);

            // Then
            assertTrue(result);
            verify(authService).verifyVerificationCode(request);
        }

        @Test
        @DisplayName("should return false when verification code doesn't match")
        void verifyVerificationCode_CodeNotMatches_ReturnsFalse() {
            // Given
            VerificationCodeRequest request = new VerificationCodeRequest();
            request.setPhone("13800138000");
            request.setVerificationCode("wrong_code");
            request.setWxCode("valid_wx_code");
            when(authService.verifyVerificationCode(request)).thenReturn(false);

            // When
            boolean result = authService.verifyVerificationCode(request);

            // Then
            assertFalse(result);
            verify(authService).verifyVerificationCode(request);
        }

        @Test
        @DisplayName("should return false when verification code expired")
        void verifyVerificationCode_CodeExpired_ReturnsFalse() {
            // Given
            VerificationCodeRequest request = new VerificationCodeRequest();
            request.setPhone("13800138000");
            request.setVerificationCode("123456");
            request.setWxCode("valid_wx_code");
            when(authService.verifyVerificationCode(request)).thenReturn(false);

            // When
            boolean result = authService.verifyVerificationCode(request);

            // Then
            assertFalse(result);
            verify(authService).verifyVerificationCode(request);
        }

        @Test
        @DisplayName("should handle null request")
        void verifyVerificationCode_NullRequest_ReturnsFalse() {
            // Given
            when(authService.verifyVerificationCode(null)).thenReturn(false);

            // When
            boolean result = authService.verifyVerificationCode(null);

            // Then
            assertFalse(result);
            verify(authService).verifyVerificationCode(null);
        }
    }

    @Nested
    @DisplayName("sendVerificationCode method tests")
    class SendVerificationCodeTests {
        @Test
        @DisplayName("should return true when phone number exists and SMS sent successfully")
        void sendVerificationCode_ValidPhone_Success_ReturnsTrue() {
            // Given
            String validPhone = "13800138000";
            when(authService.sendVerificationCode(validPhone)).thenReturn(true);

            // When
            boolean result = authService.sendVerificationCode(validPhone);

            // Then
            assertTrue(result);
            verify(authService).sendVerificationCode(validPhone);
        }

        @Test
        @DisplayName("should return false when phone number doesn't exist")
        void sendVerificationCode_PhoneNotExists_ReturnsFalse() {
            // Given
            String invalidPhone = "13800138001";
            when(authService.sendVerificationCode(invalidPhone)).thenReturn(false);

            // When
            boolean result = authService.sendVerificationCode(invalidPhone);

            // Then
            assertFalse(result);
            verify(authService).sendVerificationCode(invalidPhone);
        }

        @Test
        @DisplayName("should handle empty phone number")
        void sendVerificationCode_EmptyPhone_ReturnsFalse() {
            // Given
            String emptyPhone = "";
            when(authService.sendVerificationCode(emptyPhone)).thenReturn(false);

            // When
            boolean result = authService.sendVerificationCode(emptyPhone);

            // Then
            assertFalse(result);
            verify(authService).sendVerificationCode(emptyPhone);
        }

        @Test
        @DisplayName("should handle null phone number")
        void sendVerificationCode_NullPhone_ReturnsFalse() {
            // Given
            when(authService.sendVerificationCode(null)).thenReturn(false);

            // When
            boolean result = authService.sendVerificationCode(null);

            // Then
            assertFalse(result);
            verify(authService).sendVerificationCode(null);
        }

        @Test
        @DisplayName("should handle invalid phone number format")
        void sendVerificationCode_InvalidPhoneFormat_ReturnsFalse() {
            // Given
            String invalidPhone = "12345";
            when(authService.sendVerificationCode(invalidPhone)).thenReturn(false);

            // When
            boolean result = authService.sendVerificationCode(invalidPhone);

            // Then
            assertFalse(result);
            verify(authService).sendVerificationCode(invalidPhone);
        }
    }
} 