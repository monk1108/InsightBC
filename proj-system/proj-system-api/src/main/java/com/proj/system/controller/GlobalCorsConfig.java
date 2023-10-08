package com.proj.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @description cross-domain filter
        * @author Yinuo Yao
        * @date 2023/10/8 15:19:07
        */
@Configuration
public class GlobalCorsConfig {

    /**
     * Filters that allow cross-domain calls
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Allow whitelist domain names to make cross-domain calls
        config.addAllowedOrigin("*");
        // Allow cookies to be sent across
        config.setAllowCredentials(true);
        // Release all original header information
        config.addAllowedHeader("*");
        // Release all original header information
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
