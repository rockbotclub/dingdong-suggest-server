package cc.rockbot.dds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Configuration
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxConfig {
    private String appid;
    private String secret;

    @Autowired
    private Environment environment;

    public String getAppid() {
        return environment.getProperty("WX_APPID", appid);
    }

    public String getSecret() {
        return environment.getProperty("WX_SECRET", secret);
    }
} 