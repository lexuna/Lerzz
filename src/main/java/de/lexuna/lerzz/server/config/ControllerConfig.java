package de.lexuna.lerzz.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ControllerConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/Home.html").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/Login.html").setViewName("login");
        registry.addViewController("/Dashboard.html").setViewName("dashboard");
    }
}
