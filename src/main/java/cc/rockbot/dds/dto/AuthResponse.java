package cc.rockbot.dds.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        private String nickName;
        private String avatarUrl;
        private String organizationId;
        private String organizationName;
    }
} 