package cc.rockbot.dds.service.impl;

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
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import cc.rockbot.dds.util.JwtTokenUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
            throw new BusinessException(ErrorCode.PARAM_ERROR, "微信登录code不能为空");
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
                String errorMsg = jsonObject.getString("errmsg");
                throw new BusinessException(ErrorCode.WX_LOGIN_FAILED, errorMsg);
            }

            String openid = jsonObject.getString("openid");
            if (openid == null || openid.isEmpty()) {
                throw new BusinessException(ErrorCode.WX_OPENID_NOT_FOUND);
            }
            log.info("成功获取openid: {}", openid);

            // 查询数据库中是否存在该用户
            UserDO user = userRepository.findByWxid(openid);
            if (user == null) {
                log.info("新用户登录，openid: {}", openid);
                throw new BusinessException(ErrorCode.NEW_USER_NEED_REGISTER);
            }

            log.info("用户登录成功，openid: {}", openid);
            userVO.setUser(user);
            userVO.setToken(JwtTokenUtil.generateToken(user.getId()));
            return userVO;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("微信登录异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "微信登录异常", e);
        }
    }

    @Override
    public boolean register(UserRegisterRequest request) {
        if (request == null || request.getPhone() == null || request.getPhone().isEmpty() 
                || request.getVerificationCode() == null || request.getVerificationCode().isEmpty()
                || request.getWxid() == null || request.getWxid().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "手机号、验证码和微信openid不能为空");
        }
        
        if (!smsService.verifyCode(request.getPhone(), request.getVerificationCode())) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_ERROR);
        }

        try {
            // 检查用户是否已存在
            UserDO existingUser = userRepository.findByUserPhone(request.getPhone());
            if (existingUser == null) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }

            existingUser.setWxid(request.getWxid());
            existingUser.setGmtModified(LocalDateTime.now());
            userRepository.save(existingUser);
            
            return true;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户注册异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户注册异常", e);
        }
    }

    @Override
    public boolean sendVerificationCode(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "手机号不能为空");
        }
        
        UserDO user = userRepository.findByUserPhone(phoneNumber);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在，请联系管理员后台添加");
        }

        if (!smsService.sendVerificationCode(phoneNumber)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发送验证码失败");
        }
        
        return true;
    }
} 