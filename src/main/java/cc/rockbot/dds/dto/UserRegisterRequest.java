package cc.rockbot.dds.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterRequest {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;
    /**
     * 微信临时code
     */
    @NotBlank(message = "微信临时code不能为空")
    private String wxCode;
    
} 