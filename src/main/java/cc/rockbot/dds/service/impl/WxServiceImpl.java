package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.config.WxConfig;
import cc.rockbot.dds.dto.UpdateUserInfoRequest;
import cc.rockbot.dds.dto.WxLoginResponse;
import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.security.JwtTokenProvider;
import cc.rockbot.dds.service.WxService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class WxServiceImpl implements WxService {

    private final WxConfig wxConfig;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String WX_CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";

    @Override
    public WxLoginResponse login(String code) {
        // 调用微信接口获取openid
        String url = WX_CODE2SESSION_URL
                .replace("{appid}", wxConfig.getAppid())
                .replace("{secret}", wxConfig.getSecret())
                .replace("{code}", code);

        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSON.parseObject(response);

        if (jsonObject.containsKey("errcode") && jsonObject.getIntValue("errcode") != 0) {
            throw new RuntimeException("微信登录失败：" + jsonObject.getString("errmsg"));
        }

        String openid = jsonObject.getString("openid");
        
        // 查找或创建用户
        UserDO user = userRepository.findByWxid(openid);
        if (user == null) {
            user = new UserDO();
            user.setWxid(openid);
            user.setGmtCreate(LocalDateTime.now());
            user.setStatus(0); // 默认普通用户
            user.setUserName("");
            user.setUserOrg("");
            user.setUserPhone("");
            user.setOrgId("");
            user = userRepository.save(user);
        }

        // 生成token
        String role = user.getStatus() == 1 ? "ROLE_ADMIN" : "ROLE_USER";
        String token = jwtTokenProvider.generateToken(openid, role);

        // 构建响应
        return WxLoginResponse.builder()
                .token(token)
                .wxid(user.getWxid())
                .userName(user.getUserName())
                .userOrg(user.getUserOrg())
                .userPhone(user.getUserPhone())
                .status(user.getStatus())
                .orgId(user.getOrgId())
                .build();
    }

    @Override
    public String refreshToken(String wxid) {
        UserDO user = userRepository.findByWxid(wxid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        String role = user.getStatus() == 1 ? "ROLE_ADMIN" : "ROLE_USER";
        return jwtTokenProvider.generateToken(wxid, role);
    }

    @Override
    public WxLoginResponse updateUserInfo(String wxid, UpdateUserInfoRequest request) {
        UserDO user = userRepository.findByWxid(wxid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息
        user.setUserName(request.getUserName());
        user.setUserOrg(request.getUserOrg());
        user.setUserPhone(request.getUserPhone());
        user.setOrgId(request.getOrgId());
        user.setGmtModified(LocalDateTime.now());
        user = userRepository.save(user);

        // 生成新的token
        String role = user.getStatus() == 1 ? "ROLE_ADMIN" : "ROLE_USER";
        String token = jwtTokenProvider.generateToken(wxid, role);

        // 构建响应
        return WxLoginResponse.builder()
                .token(token)
                .wxid(user.getWxid())
                .userName(user.getUserName())
                .userOrg(user.getUserOrg())
                .userPhone(user.getUserPhone())
                .status(user.getStatus())
                .orgId(user.getOrgId())
                .build();
    }
} 