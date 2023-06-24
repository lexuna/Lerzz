package de.lexuna.lerzz.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for the Webservice
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Method to configure the Cross-Origin Resource Sharing for specific endpoints
     *
     * @param registry the CORS-object to add cors-configuration
     */
    @Override
    public final void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/deck/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                .allowedHeaders("*");
    }
}

