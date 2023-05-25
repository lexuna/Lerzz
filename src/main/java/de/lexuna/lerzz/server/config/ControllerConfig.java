package de.lexuna.lerzz.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class is a Spring configuration class that provides configuration for view controllers in a Spring web application.
 * It implements the {@link WebMvcConfigurer} interface to customize the configuration of the Spring MVC web application.
 */
@Configuration
public class ControllerConfig implements WebMvcConfigurer {

    /**
     * This method overrides the default implementation in {@link WebMvcConfigurer} to add custom view controllers.
     * It adds several view controllers that map specific URLs to view names in the application.
     *
     * @param registry the {@link ViewControllerRegistry} to register view controllers with
     */
    @Override
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
