package cc.rockbot.dds.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WxLoginRequest {
    @NotBlank(message = "微信登录code不能为空")
    private String code;
} 