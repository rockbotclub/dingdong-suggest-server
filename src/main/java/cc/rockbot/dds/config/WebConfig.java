package cc.rockbot.dds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

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
                        "http://springboot-wu96-152263-4-1352937363.sh.run.tcloudbase.com",
                        "https://springboot-wu96-152263-4-1352937363.sh.run.tcloudbase.com",
                        "http://localhost",
                        "https://localhost"
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);

                // Allow CORS for Swagger UI
                registry.addMapping("/swagger-ui/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "OPTIONS")
                    .allowedHeaders("*")
                    .maxAge(3600);

                // Allow CORS for API docs
                registry.addMapping("/v3/api-docs/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "OPTIONS")
                    .allowedHeaders("*")
                    .maxAge(3600);
            }
        };
    }
} 