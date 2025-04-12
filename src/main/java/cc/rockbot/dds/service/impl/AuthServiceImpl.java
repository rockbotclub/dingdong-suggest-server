package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.dto.AuthRequest;
import cc.rockbot.dds.dto.AuthResponse;
import cc.rockbot.dds.dto.VerificationCodeRequest;
import cc.rockbot.dds.service.AuthService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserRepository userRepository;

    @Override
    public AuthResponse login(AuthRequest request) {
        AuthResponse response = new AuthResponse();
        // 查询数据库中是否存在该用户
        UserDO user = userRepository.findByWxid(request.getCode());
        if (user == null) {
            response.setSuccess(true);
            response.setMessage("用户不存在");
            return response;
        }
        response.setSuccess(true);
        response.setData(convertToUserInfo(user));
        return response;
    }

    @Override
    public void sendVerificationCode(VerificationCodeRequest request) {
        // TODO: Implement SMS verification code sending logic
    }

    private AuthResponse.UserInfo convertToUserInfo(UserDO user) {
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setNickName(user.getUserName());
        userInfo.setAvatarUrl("https://example.com/avatar.jpg");
        userInfo.setOrganizationId(user.getOrgId());
        userInfo.setOrganizationName(user.getUserOrg());
        return userInfo;
    }
} 