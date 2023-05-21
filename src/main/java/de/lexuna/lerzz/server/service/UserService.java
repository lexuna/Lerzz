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

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User registerNewUserAccount(User userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.getRoles().add("USER");

        return repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }

    public User findUserByEmail(String mail) {
        return repository.findByEmail(mail);
    }

    public User findUserById(String userId) {
        return repository.findById(userId).get();
    }
    public static UserDTO getEmptyDTO() {
        return new UserDTO();
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public User findUserByName(String username) {
        return repository.findByUsername(username);
    }

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
