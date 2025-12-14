package com.example.claims.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



/**
 * Web configuration for CORS settings.
 * <p>
 * This class is not intended for extension.
 */
@Configuration
public class WebConfig {
    /**
     * Provides a WebMvcConfigurer bean to configure CORS.
     *
     * @return a WebMvcConfigurer instance
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Configure CORS mappings.
             *
             * @param registry the CORS registry
             */
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods(
                            "GET", "POST", "PUT", "DELETE", "OPTIONS"
                        )
                        .allowedHeaders("*");
            }
        };
    }
}
