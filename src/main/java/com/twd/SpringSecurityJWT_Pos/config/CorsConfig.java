package com.twd.SpringSecurityJWT_Pos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/auth/get_image/**")
                .allowedMethods("/auth/users/**")
                .allowedOrigins("*") // Allow all origins; specify your frontend URL for better security
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false); // Change to true if credentials are required
    }
}