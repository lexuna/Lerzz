package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Service responsible for registering new user accounts and checking if a given email already exists in the system.
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Registers a new user account with the given user data.
     *
     * @param userDto the user data to register
     * @return the registered user object
     *
     */
    public User registerNewUserAccount(User userDto) {
        if (emailExists(userDto.getEmail())) {
            throw new IllegalArgumentException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.getRoles().add("USER");

        return repository.save(user);
    }

    /**
     * Checks if a given email address already exists in the system.
     *
     * @param email the email address to check
     * @return {@code true} if the email already exists, {@code false} otherwise
     */
    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }

    /**
     * Finds the user with the given email
     * @param mail the email address to find
     * @return User
     */
    public User findUserByEmail(String mail) {
        return repository.findByEmail(mail);
    }

    /**
     * Finds the user with the given id
     * @param userId String
     * @return user
     */
    public User findUserById(String userId) {
        return repository.findById(userId).get();
    }

    /**
     * turs a User to a UserDTO
     * @param user User
     * @return UserDTO
     */
    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    /**
     * finds a user by name
     * @param username String
     * @return User
     */
    public User findUserByName(String username) {
        return repository.findByUsername(username);
    }

    /**
     * User DTO
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO implements Serializable {
        private String id;
        private String name;
        private String email;
    }
}
