package com.eco.ecosystem.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Define which origins are allowed to access the application
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://192.168.112:3000",
                "https://roodek.github.io/ecosysFront/")); // Add your frontend origin here
        // Allow credentials if your frontend needs them (e.g., cookies, authorization headers)
        corsConfiguration.setAllowCredentials(true);

        // Define allowed methods (GET, POST, PUT, DELETE, etc.)
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Define allowed headers
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));

        // Define exposed headers (optional, e.g., "Authorization" if you need it)
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        // Set up the configuration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply to all paths

        return new CorsFilter(source);
    }
}
