package cc.rockbot.dds.dto;
import java.io.Serializable;
import lombok.Data;
@Data
public class UserRegisterDTO implements Serializable {
    private String wxid;
    private String jwtToken;
}
