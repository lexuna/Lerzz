package de.lexuna.lerzz.server.controller;

import com.mongodb.MongoBulkWriteException;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.UserRepository;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * https://www.bezkoder.com/spring-boot-login-example-mysql/
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository repo;

    @PostMapping("/signin")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        Optional<User> result = repo.findById(request.mail());
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("Unknown user"));
        }
        User user = result.get();
        if (!user.getPassword().equals(request.password())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("Wrong password"));
        }
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> create(@RequestBody User user) {
        log.info("Create new user");
        try {
            if (repo.findById(user.getMail()).isEmpty()) {
                return ResponseEntity.ok(new UserResponse(repo.insert(user)));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("User already exists"));
            }
        } catch (MongoBulkWriteException e) {
            log.error("Failed to create user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("Failed to create User"));
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<UserResponse> logout(@RequestBody User user) {
        return null; //TODO implement
    }

    @PutMapping("")
    public ResponseEntity<UserResponse> update(@RequestParam(value = "mail") String mail,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "password", required = false) String password) {

        Optional<User> response = repo.findById(mail);
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("Unknown user"));
        }
        User user = response.get();
        if (name != null && !name.isBlank()) {
            user.setName(name);
            repo.save(user);
        }
        if (password != null && !password.isBlank()) {
            user.setName(password);
            repo.save(user);
        }
        return ResponseEntity.ok(new UserResponse(user));
    }

    @DeleteMapping("/{mail}")
    public void delete(@PathVariable String mail) {
        repo.deleteById(mail);
    }

    record UserResponse(String message, User user) {
        UserResponse(String message) {
            this(message, null);
        }

        UserResponse(User user) {
            this(null, user);
        }
    }

    record LoginRequest(String mail, String password) {
    }
}
