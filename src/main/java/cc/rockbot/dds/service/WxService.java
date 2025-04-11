package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.UpdateUserInfoRequest;
import cc.rockbot.dds.dto.WxLoginResponse;

public interface WxService {
    /**
     * 微信登录
     *
     * @param code 微信登录code
     * @return 登录响应，包含token和用户信息
     */
    WxLoginResponse login(String code);

    /**
     * 刷新token
     *
     * @param wxid 微信ID
     * @return 新的token
     */
    String refreshToken(String wxid);

    /**
     * 更新用户信息
     *
     * @param wxid 微信ID
     * @param request 用户信息
     * @return 更新后的用户信息
     */
    WxLoginResponse updateUserInfo(String wxid, UpdateUserInfoRequest request);
} 