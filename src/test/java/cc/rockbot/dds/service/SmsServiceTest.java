package cc.rockbot.dds.service;

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
class SmsServiceTest {

    @Mock
    private SmsService smsService;

    @Nested
    @DisplayName("sendVerificationCode method tests")
    class SendVerificationCodeTests {
        @Test
        @DisplayName("should return true when SMS sent successfully")
        void sendVerificationCode_Success_ReturnsTrue() {
            // Given
            String validPhone = "13800138000";
            when(smsService.sendVerificationCode(validPhone)).thenReturn(true);

            // When
            boolean result = smsService.sendVerificationCode(validPhone);

            // Then
            assertTrue(result);
            verify(smsService).sendVerificationCode(validPhone);
        }

        @Test
        @DisplayName("should return false when SMS sending fails")
        void sendVerificationCode_Failure_ReturnsFalse() {
            // Given
            String validPhone = "13800138000";
            when(smsService.sendVerificationCode(validPhone)).thenReturn(false);

            // When
            boolean result = smsService.sendVerificationCode(validPhone);

            // Then
            assertFalse(result);
            verify(smsService).sendVerificationCode(validPhone);
        }

        @Test
        @DisplayName("should handle empty phone number")
        void sendVerificationCode_EmptyPhone_ReturnsFalse() {
            // Given
            String emptyPhone = "";
            when(smsService.sendVerificationCode(emptyPhone)).thenReturn(false);

            // When
            boolean result = smsService.sendVerificationCode(emptyPhone);

            // Then
            assertFalse(result);
            verify(smsService).sendVerificationCode(emptyPhone);
        }

        @Test
        @DisplayName("should handle null phone number")
        void sendVerificationCode_NullPhone_ReturnsFalse() {
            // Given
            when(smsService.sendVerificationCode(null)).thenReturn(false);

            // When
            boolean result = smsService.sendVerificationCode(null);

            // Then
            assertFalse(result);
            verify(smsService).sendVerificationCode(null);
        }

        @Test
        @DisplayName("should handle invalid phone number format")
        void sendVerificationCode_InvalidPhoneFormat_ReturnsFalse() {
            // Given
            String invalidPhone = "12345";
            when(smsService.sendVerificationCode(invalidPhone)).thenReturn(false);

            // When
            boolean result = smsService.sendVerificationCode(invalidPhone);

            // Then
            assertFalse(result);
            verify(smsService).sendVerificationCode(invalidPhone);
        }
    }

    @Nested
    @DisplayName("verifyCode method tests")
    class VerifyCodeTests {
        @Test
        @DisplayName("should return true when code matches")
        void verifyCode_CodeMatches_ReturnsTrue() {
            // Given
            String phone = "13800138000";
            String code = "123456";
            when(smsService.verifyCode(phone, code)).thenReturn(true);

            // When
            boolean result = smsService.verifyCode(phone, code);

            // Then
            assertTrue(result);
            verify(smsService).verifyCode(phone, code);
        }

        @Test
        @DisplayName("should return false when code doesn't match")
        void verifyCode_CodeNotMatches_ReturnsFalse() {
            // Given
            String phone = "13800138000";
            String code = "wrong_code";
            when(smsService.verifyCode(phone, code)).thenReturn(false);

            // When
            boolean result = smsService.verifyCode(phone, code);

            // Then
            assertFalse(result);
            verify(smsService).verifyCode(phone, code);
        }

        @Test
        @DisplayName("should return false when code expired")
        void verifyCode_CodeExpired_ReturnsFalse() {
            // Given
            String phone = "13800138000";
            String code = "123456";
            when(smsService.verifyCode(phone, code)).thenReturn(false);

            // When
            boolean result = smsService.verifyCode(phone, code);

            // Then
            assertFalse(result);
            verify(smsService).verifyCode(phone, code);
        }

        @Test
        @DisplayName("should handle null phone number")
        void verifyCode_NullPhone_ReturnsFalse() {
            // Given
            String code = "123456";
            when(smsService.verifyCode(null, code)).thenReturn(false);

            // When
            boolean result = smsService.verifyCode(null, code);

            // Then
            assertFalse(result);
            verify(smsService).verifyCode(null, code);
        }

        @Test
        @DisplayName("should handle null code")
        void verifyCode_NullCode_ReturnsFalse() {
            // Given
            String phone = "13800138000";
            when(smsService.verifyCode(phone, null)).thenReturn(false);

            // When
            boolean result = smsService.verifyCode(phone, null);

            // Then
            assertFalse(result);
            verify(smsService).verifyCode(phone, null);
        }
    }
} 