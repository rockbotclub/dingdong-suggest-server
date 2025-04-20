package cc.rockbot.dds.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    @NotBlank(message = "微信openid不能为空")
    private String wxid;
    
} 