package de.lexuna.lerzz.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the application.
 */
@Data
@NoArgsConstructor
@Document
public class User implements Serializable {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();

    /**
     * Constructor
     * @param email of the user
     * @param username of the user
     * @param password of the user
     */
    public User(final String email,
                final String username,
                final String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
