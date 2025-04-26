package cc.rockbot.dds.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {
    /**
     * 微信临时code
     */
    @NotBlank(message = "微信临时code不能为空")
    private String wxCode;
}
