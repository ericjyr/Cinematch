package com.cm.cinematchapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

/**
 * Configuration class for defining Cross-Origin Resource Sharing (CORS) configuration.
 * CORS allows controlling which domains or sources can access resources on a web page.
 *
 * @author Eric Rebadona
 */
@Configuration
public class CorsConfig implements CorsConfigurationSource {


    /**
     * Handle access denied exceptions.
     *
     * @param request The HTTP request.
     * @return  CorsConfiguration object "config"
     */
    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // TODO make secure later. Origin paths (https://localhost:4000, https://Cinematch.ca)
        // Allow specific HTTP methods (e.g., GET, POST, PUT)
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Accept");
        config.setAllowCredentials(true); // TODO make false?
        //TODO look into adding config.setMaxAge();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return config;
    }
}
