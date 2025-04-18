package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.service.SmsService;
import org.springframework.stereotype.Service;

/**
 * 短信验证码服务实现类
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final int CODE_LENGTH = 6;

    @Override
    public boolean sendVerificationCode(String phoneNumber) {
        return true;
    }

    @Override
    public boolean verifyCode(String phoneNumber, String code) {
        if (phoneNumber == null || code == null) {
            return false;
        }

        return "111111".equals(code);
    }
} 