package cc.rockbot.dds.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;

/**
 * Swagger配置类
 * 用于配置API文档的生成
 */
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("叮咚建议服务 API文档")
                        .description("叮咚建议服务的API文档，包含所有接口的详细说明")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RockOps")
                                .email("rockops@example.com")
                                .url("https://github.com/rockops"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addServersItem(new Server().url("/"));
    }
} 