package cc.rockbot.dds.service;

/**
 * 短信验证码服务接口
 */
public interface SmsService {
    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号码
     * @return 发送结果
     *     * true: 发送成功
     *     * false: 发送失败
     */
    boolean sendVerificationCode(String phoneNumber);

    /**
     * 验证短信验证码
     *
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return 验证结果
     *     * true: 验证成功
     *     * false: 验证失败
     */
    boolean verifyCode(String phoneNumber, String code);
} 