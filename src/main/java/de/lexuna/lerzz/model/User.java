package de.lexuna.lerzz.model;

import lombok.*;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    String email;
    String username;
    String password;
    List<String> roles = new ArrayList<>();

}
