package de.lexuna.lerzz.model;

import lombok.*;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the application.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class User {

    /**
     * The email address of the user. This field is used as the identifier for the object when it is persisted in the
     * database.
     */
    @Id
    String email;

    /**
     * The username of the user.
     */
    String username;

    /**
     * The password of the user.
     */
    String password;

    /**
     * A list of roles assigned to the user.
     */
    List<String> roles = new ArrayList<>();

}
