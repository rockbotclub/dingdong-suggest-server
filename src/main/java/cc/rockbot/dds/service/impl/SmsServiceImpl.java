package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证码服务实现类
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRE_MINUTES = 5;

    

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