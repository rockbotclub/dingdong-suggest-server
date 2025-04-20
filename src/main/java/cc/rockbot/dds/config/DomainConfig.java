package cc.rockbot.dds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "domain")
public class DomainConfig {
    private String baseUrl;
} 