package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.AuthResponse;
import cc.rockbot.dds.dto.VerificationCodeRequest;
import cc.rockbot.dds.service.AuthService;
import cc.rockbot.dds.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.config.WxConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private WxConfig wxConfig;

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private SmsService smsService;

    private static final String WX_CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";

    @Override
    public UserDO login(String wxCode) {
        // TODO: 实现微信登录逻辑
        return null;
    }

    @Override
    public boolean verifyVerificationCode(VerificationCodeRequest request) {
        if (request == null || request.getPhone() == null || request.getVerificationCode() == null) {
            return false;
        }
        
        return smsService.verifyCode(request.getPhone(), request.getVerificationCode());
    }

    @Override
    public boolean sendVerificationCode(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        
        return smsService.sendVerificationCode(phoneNumber);
    }
} 