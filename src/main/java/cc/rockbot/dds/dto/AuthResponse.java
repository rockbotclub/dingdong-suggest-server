package cc.rockbot.dds.dto;

import lombok.Data;
import cc.rockbot.dds.model.UserDO;

@Data
public class AuthResponse {
    private boolean success;
    private String message;
    private UserDO data;

    public AuthResponse(boolean success, String message, UserDO data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}   