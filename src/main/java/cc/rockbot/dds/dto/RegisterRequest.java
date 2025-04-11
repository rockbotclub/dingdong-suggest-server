package cc.rockbot.dds.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "部门不能为空")
    private String department;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    @NotBlank(message = "微信openid不能为空")
    private String wxid;
} 