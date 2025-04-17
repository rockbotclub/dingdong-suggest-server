package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.UserRegisterRequest;
import cc.rockbot.dds.dto.UserVO;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"/test_data.sql"})
@Transactional
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_ExistingUser_UpdatesWxid() {
        // Given: 使用 test_data.sql 中已存在的用户数据
        String existingPhone = "13900139001";
        String newWxid = "new_wxid_001";
        String verificationCode = "111111"; // 使用固定的验证码

        // 先发送验证码
        authService.sendVerificationCode(existingPhone);

        UserRegisterRequest request = new UserRegisterRequest();
        request.setPhone(existingPhone);
        request.setVerificationCode(verificationCode);
        request.setWxid(newWxid);

        // When: 注册已存在的用户
        boolean result = authService.register(request);

        // Then: 验证wxid被更新
        assertTrue(result);
        UserDO updatedUser = userRepository.findByUserPhone(existingPhone);
        assertNotNull(updatedUser);
        assertEquals(newWxid, updatedUser.getWxid());
    }

    @Test
    void sendVerificationCode_ExistingUser_Success() {
        // Given: 使用 test_data.sql 中的用户数据
        String existingPhone = "13900139001";

        // When: 发送验证码
        boolean result = authService.sendVerificationCode(existingPhone);

        // Then: 验证发送结果
        assertTrue(result);
    }

    @Test
    void sendVerificationCode_NonExistentUser_ThrowsException() {
        // Given: 使用不存在的手机号
        String nonExistentPhone = "13900000000";

        // When & Then: 验证发送验证码给不存在的用户会抛出异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.sendVerificationCode(nonExistentPhone);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void register_InvalidVerificationCode_ThrowsException() {
        // Given: 使用无效的验证码
        UserRegisterRequest request = new UserRegisterRequest();
        request.setPhone("13900139001");
        request.setVerificationCode("invalid_code");
        request.setWxid("new_wxid");

        // When & Then: 验证使用无效验证码注册会抛出异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(request);
        });

        assertEquals(ErrorCode.VERIFICATION_CODE_ERROR.getCode(), exception.getErrorCode().getCode());
    }
} 