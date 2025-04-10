package cc.rockbot.dds.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource(value = "file:${user.dir}/.env", ignoreResourceNotFound = true),
    @PropertySource(value = "classpath:.env", ignoreResourceNotFound = true)
})
public class EnvConfig {

    @Autowired
    private Environment env;

    @Bean
    public String printEnv() {
        String envInfo = String.format("MYSQL_ADDRESS: %s, MYSQL_USERNAME: %s, MYSQL_PASSWORD: %s",
            env.getProperty("MYSQL_ADDRESS"),
            env.getProperty("MYSQL_USERNAME"),
            env.getProperty("MYSQL_PASSWORD"));
        System.out.println(envInfo);
        return envInfo;
    }
} 