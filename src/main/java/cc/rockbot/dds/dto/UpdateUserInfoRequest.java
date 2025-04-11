package cc.rockbot.dds.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserInfoRequest {
    @NotBlank(message = "用户姓名不能为空")
    private String userName;

    @NotBlank(message = "用户组织不能为空")
    private String userOrg;

    @NotBlank(message = "用户手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String userPhone;

    @NotBlank(message = "组织ID不能为空")
    private String orgId;
} 