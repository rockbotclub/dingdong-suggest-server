package cc.rockbot.dds.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final DomainConfig domainConfig;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allow CORS for API endpoints
                registry.addMapping("/api/**")
                    .allowedOrigins(
                        "http://" + domainConfig.getBaseUrl(),
                        "https://" + domainConfig.getBaseUrl(),
                        "http://localhost",
                        "https://localhost"
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);

                // Allow CORS for Swagger UI
                registry.addMapping("/swagger-ui/**")
                    .allowedOrigins(
                        "http://" + domainConfig.getBaseUrl(),
                        "https://" + domainConfig.getBaseUrl(),
                        "http://localhost",
                        "https://localhost"
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);

                // Allow CORS for API docs
                registry.addMapping("/v3/api-docs/**")
                    .allowedOrigins(
                        "http://" + domainConfig.getBaseUrl(),
                        "https://" + domainConfig.getBaseUrl(),
                        "http://localhost",
                        "https://localhost"
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
} 