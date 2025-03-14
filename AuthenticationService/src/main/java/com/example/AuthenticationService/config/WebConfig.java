package com.example.AuthenticationService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allows all endpoints to have CORS
                .allowedOriginPatterns("http://localhost:*") // Allow all localhost origins with any port
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allows all headers
                .allowCredentials(true); // Allows credentials (e.g., cookies, authorization headers)

        // Uncomment the below line to restrict to a specific frontend
        // .allowedOrigins("http://your-frontend-domain.com");
    }
}


