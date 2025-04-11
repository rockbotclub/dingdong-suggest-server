package cc.rockbot.dds.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WxLoginResponse {
    private String token;
    private String wxid;
    private String userName;
    private String userOrg;
    private String userPhone;
    private Integer status;
    private String orgId;
} 