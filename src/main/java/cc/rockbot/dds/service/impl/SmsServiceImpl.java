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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean sendVerificationCode(String phoneNumber) {
        // 生成6位随机验证码
        String code = generateRandomCode();
        
        // TODO: 调用实际的短信发送服务
        // 这里只是模拟发送成功
        boolean sendSuccess = true;
        
        if (sendSuccess) {
            // 将验证码存入Redis，设置5分钟过期
            String key = SMS_CODE_PREFIX + phoneNumber;
            redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean verifyCode(String phoneNumber, String code) {
        String key = SMS_CODE_PREFIX + phoneNumber;
        String storedCode = redisTemplate.opsForValue().get(key);
        
        if (storedCode == null) {
            return false;
        }
        
        boolean matches = storedCode.equals(code);
        if (matches) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
        }
        
        return matches;
    }

    /**
     * 生成指定长度的随机数字验证码
     */
    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 