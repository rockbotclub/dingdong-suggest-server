package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.dto.SendVerificationRequest;
import cc.rockbot.dds.dto.UserRegisterRequest;
import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.service.AuthService;
import cc.rockbot.dds.service.SmsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cc.rockbot.dds.dto.UserVO;

import javax.annotation.Resource;
import cc.rockbot.dds.util.JwtTokenUtil;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserRepository userRepository;

    @Value("${wx.miniapp.appid}")
    private String appid;

    @Value("${wx.miniapp.secret}")
    private String secret;

    private static final String WX_CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";

    @Override
    public UserVO login(String wxCode) {
        if (wxCode == null || wxCode.isEmpty()) {
            log.warn("微信登录失败：wxCode为空");
            return null;
        }

        UserVO userVO = new UserVO();

        try {
            // 调用微信接口获取openid
            String url = WX_CODE2SESSION_URL
                    .replace("{appid}", appid)
                    .replace("{secret}", secret)
                    .replace("{code}", wxCode);

            String wxResponse = restTemplate.getForObject(url, String.class);
            log.info("微信登录请求，url: {}, 响应: {}", url, wxResponse);
            
            JSONObject jsonObject = JSON.parseObject(wxResponse);

            // 检查微信接口返回的错误
            if (jsonObject.containsKey("errcode") && jsonObject.getIntValue("errcode") != 0) {
                String errorMsg = "微信登录失败：" + jsonObject.getString("errmsg");
                log.error(errorMsg);
                return null;
            }

            String openid = jsonObject.getString("openid");
            if (openid == null || openid.isEmpty()) {
                log.error("微信登录失败：未获取到openid");
                return null;
            }
            
            // 查询数据库中是否存在该用户
            UserDO user = userRepository.findByWxid(openid);
            if (user == null) {
                log.info("新用户登录，openid: {}", openid);
                return null;
            }

            log.info("用户登录成功，openid: {}", openid);
            userVO.setUser(user);
            userVO.setToken(JwtTokenUtil.generateToken(user.getId()));
            return userVO;
        } catch (Exception e) {
            log.error("微信登录异常", e);
            return null;
        }
    }

    @Override
    public boolean register(UserRegisterRequest request) {
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
        UserDO user = userRepository.findByUserPhone(phoneNumber);
        if (user == null) {
            throw new RuntimeException("用户不存在，请联系管理员后台添加");
        }

        return smsService.sendVerificationCode(phoneNumber);
    }
} 