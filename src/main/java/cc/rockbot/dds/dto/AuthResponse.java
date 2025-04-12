package cc.rockbot.dds.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private boolean success;
    private String message;
    private UserInfo data;

    @Data
    public static class UserInfo {
        private String nickName;
        private String avatarUrl;
        private String organizationId;
        private String organizationName;
    }
} 