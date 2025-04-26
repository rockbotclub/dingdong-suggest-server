package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.service.SmsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import cc.rockbot.dds.util.JwtTokenService;
import cc.rockbot.dds.service.UserService;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import cc.rockbot.dds.dto.UserRegisterDTO;
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Value("${wx.miniapp.appid}")
    private String appid;

    @Value("${wx.miniapp.secret}")
    private String secret;

    private static final String WX_CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";

    @Override
    public UserRegisterDTO loginByWxCode(String wxCode) {
        if (wxCode == null || wxCode.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "微信临时code不能为空");
        }

        try {
            // 获取微信ID
            String wxId = getWxIdByWxCode(wxCode);
            if (wxId == null || wxId.isEmpty()) {
                throw new BusinessException(ErrorCode.WX_OPENID_NOT_FOUND);
             }
            log.info("成功获取openid: {}", wxId);

            // 查询数据库中是否存在该用户
            List<UserDO> users = userRepository.findByWxid(wxId);
            if (users == null || users.isEmpty()) {
                log.info("新用户登录，openid: {}", wxId);
                throw new BusinessException(ErrorCode.NEW_USER_NEED_REGISTER);
            }

            UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
            userRegisterDTO.setJwtToken(jwtTokenService.generateTokenIfNotExist(wxId));
            userRegisterDTO.setWxid(wxId);
            return userRegisterDTO;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("微信登录异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "微信登录异常", e);
        }
    }

    /**
     * 根据微信临时code获取微信ID
     * @param wxCode
     * @throws BusinessException
     * @return
     */
    private String getWxIdByWxCode(String wxCode) throws BusinessException {
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

        return jsonObject.getString("openid");
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

    @Override
    public UserDO createUser(UserDO user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户对象不能为空");
        }
        user.setGmtCreate(LocalDateTime.now());
        user.setGmtModified(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public Optional<UserDO> getUserById(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户ID不能为空");
        }
        return userRepository.findById(id);
    }

    @Override
    public List<UserDO> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDO updateUser(UserDO user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户对象不能为空");
        }
        if (user.getId() == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户ID不能为空");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        user.setGmtModified(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户ID不能为空");
        }
        if (!userRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDO getUserByWxid(String wxid) {
        if (wxid == null || wxid.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "微信ID不能为空");
        }
        List<UserDO> users = userRepository.findByWxid(wxid);
        if (users == null || users.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        return users.get(0);
    }

    @Override
    public boolean existsByWxid(String wxid) {
        if (wxid == null || wxid.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "微信ID不能为空");
        }
        return userRepository.existsByWxid(wxid);
    }

    @Override
    public List<UserDO> getUsersByOrgId(String orgId) {
        if (orgId == null || orgId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "组织ID不能为空");
        }
        return userRepository.findByOrgId(orgId);
    }

    @Override
    public UserRegisterDTO register(UserDO userDO, String wxCode) {
        try {
            // 获取微信ID
            String wxId = getWxIdByWxCode(wxCode);
            if (wxId == null || wxId.isEmpty()) {
                throw new BusinessException(ErrorCode.WX_OPENID_NOT_FOUND);
            }
            log.info("成功获取openid: {}", wxId);

            // 设置用户信息
            userDO.setWxid(wxId);
            userRepository.save(userDO);

            UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
            userRegisterDTO.setJwtToken(jwtTokenService.generateTokenIfNotExist(wxId));
            userRegisterDTO.setWxid(wxId);
            return userRegisterDTO;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("微信登录异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "微信登录异常", e);
        }
    }

} 