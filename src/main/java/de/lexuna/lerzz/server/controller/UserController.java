package de.lexuna.lerzz.server.controller;

import com.mongodb.MongoBulkWriteException;
import de.lexuna.lerzz.modle.User;
import de.lexuna.lerzz.modle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository repo;

    @GetMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestParam(value = "mail") String mail, @RequestParam(value = "password") String password) {
        Optional<User> result = repo.findById(mail);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("Unknown user"));
        }
        User user = result.get();
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("Wrong password"));
        }
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("")
    public ResponseEntity<UserResponse> create(@RequestParam(value = "mail") String mail,
                                               @RequestParam(value = "name") String name,
                                               @RequestParam(value = "password") String password) {
        try {
            return ResponseEntity.ok(new UserResponse(repo.insert(new User(mail, name, password))));
        } catch (MongoBulkWriteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserResponse("User already exists"));
        }
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

    @DeleteMapping("")
    public void delete(@RequestParam(value = "mail") String mail) {
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
}
