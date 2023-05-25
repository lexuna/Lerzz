package de.lexuna.lerzz.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ControllerConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/dashboard").setViewName("dashboard");
        registry.addViewController("/about").setViewName("about");
        registry.addViewController("/kontakt").setViewName("kontakt");
        registry.addViewController("/help").setViewName("help");
        registry.addViewController("/create_quiz").setViewName("create_quiz");
        registry.addViewController("/quiz").setViewName("quiz");
        registry.addViewController("/quiz_results").setViewName("quiz_results");
        registry.addViewController("/quiz_result_users").setViewName("quiz_result_users");

//        registry.addViewController("/edit_stack").setViewName("edit_stack");

    }
}
