package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.UserVO;
import cc.rockbot.dds.dto.UserRegisterRequest;

public interface AuthService {
    /**
     * 微信登录
     * 用户在客户端调用微信的login接口获取一个code作为临时token，放在AuthRequest对象中
     * 服务器端根据此code获取用户的微信openid(wxid)
     * 根据wxid查数据库的User表，如果数据库中没有记录，则要让用户验证手机号。
     * 
     * @param request
     * @return
     *      * 如果数据库中没有记录，则返回false
     *      * 如果数据库中有记录，则返回true, 并返回用户信息
     */
    UserVO login(String wxCode);

    /**
     * 验证手机号和验证码是否一致
     * 
     * 根据手机号、验证码去Redis里边是否存在
     * 如果存在，则比对跟request里的验证码是否一致
     * 
     * @param request
     * @return
     *      * 如果一致，则返回true, 删除redis里的验证码，并且要把用户的openId写入数据库User表wxid字段
     *      * 如果不一致，则返回false
     *      * 如果redis里边不存在，则返回false
     */
    boolean register(UserRegisterRequest request);

    /**
     * 发送验证码
     * 
     * 根据手机号查询User表中手机号是否存在
     *      * 如果存在，则调用发短信的接口，发送验证码
     *      * 如果不存在，则返回"用户不存在，请联系管理员后台添加"
     * 验证码要放在redis中，有效期为1分钟

     * @param phoneNumber
     * @return 
     *     * 发送验证码成功后，返回true
     *     * 发送验证码失败，返回false
     */
    boolean sendVerificationCode(String phoneNumber);
} 
