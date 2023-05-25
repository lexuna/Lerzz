package de.lexuna.lerzz.server.config;

import de.lexuna.lerzz.server.service.LerzzUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security. Defines the authorization and authentication rules for the application,
 * as well as the user details service and password encoder beans.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * The user details service that will be used to retrieve user information for authentication.
     */
    @Autowired
    private LerzzUserDetailsService userDetailsService;

    /**
     * Defines the security filter chain for the application. Configures authorization and authentication rules.
     * @param http the HttpSecurity object used to define security configuration
     * @return a SecurityFilterChain object that represents the configured security filter chain
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests // "/", "/home", "/index"
                        .requestMatchers("/home","/js/*", "/kontakt", "/css/*", "/images/*", "/", "/help", "/register").permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .successForwardUrl("/dashboard")
                        .permitAll())
                .logout((logout) -> logout.permitAll())
                .httpBasic();
        return http.build();
    }

    /**
     * Returns the user details service bean that will be used by Spring Security to retrieve user information
     * for authentication.
     * @return the user details service bean
     */
    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    /**
     * Returns the password encoder bean that will be used by Spring Security to encode and compare passwords.
     * @return the password encoder bean
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

