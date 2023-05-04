package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Custom authentication provider for authenticating users against a third-party system.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    public CustomAuthenticationProvider(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Authenticates a user based on the provided authentication information.
     *
     * @param authentication the authentication information
     * @return the authentication token if the user was successfully authenticated, null otherwise
     * @throws AuthenticationException if there was an error while authenticating the user
     */
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userRepo.findByEmail(name);

        if (encoder.matches(password, user.getPassword())) { //ToDO shouldAuthenticateAgainstThirdPartySystem()

            // use the credentials
            // and authenticate against the third-party system
            return new UsernamePasswordAuthenticationToken(
                    name, password, new ArrayList<>());
        } else {
            return null;
        }
    }

    /**
     * Indicates whether this authentication provider supports the specified authentication token.
     *
     * @param authentication the authentication token to check
     * @return true if this authentication provider supports the specified authentication token, false otherwise
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
