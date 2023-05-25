package de.lexuna.lerzz.model;

import lombok.*;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the application.
 */
@Data
@NoArgsConstructor
@Document
public class User {

    @Id
    String id;

    @Indexed(unique = true)
    String email;
    @Indexed(unique = true)
    String username;
    String password;
    List<String> roles = new ArrayList<>();

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
