package cc.rockbot.dds.service;


import org.springframework.stereotype.Service;

import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.dto.UserRegisterDTO;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

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
    UserRegisterDTO loginByWxCode(String wxCode);


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

    /**
     * 创建用户
     * @param user 用户对象
     * @return 创建后的用户对象
     */
    UserDO createUser(UserDO user);

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回空
     */
    Optional<UserDO> getUserById(Long id);

    /**
     * 获取所有用户
     * @return 用户列表
     */
    List<UserDO> getAllUsers();

    /**
     * 更新用户
     * @param user 用户对象
     * @return 更新后的用户对象
     */
    UserDO updateUser(UserDO user);

    /**
     * 删除用户
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 根据微信ID获取用户
     * @param wxid 微信ID
     * @return 用户对象，如果不存在则返回null
     */
    UserDO getUserByWxid(String wxid);

    /**
     * 检查微信ID是否存在
     * @param wxid 微信ID
     * @return 是否存在
     */
    boolean existsByWxid(String wxid);

    /**
     * 根据组织ID获取用户列表
     * @param orgId 组织ID
     * @return 用户列表
     */
    List<UserDO> getUsersByOrgId(String orgId);

    /**
     * 保存用户并生成JWT token
     * @param userDO 用户对象
     * @param wxCode 微信临时code
     * @return 用户注册DTO
     */
    UserRegisterDTO register(UserDO userDO, String wxCode);
} 