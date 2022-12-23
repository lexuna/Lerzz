package de.lexuna.lerzz.modle;

import lombok.Setter;
import lombok.Value;
import org.springframework.data.annotation.Id;

@Value
public class User {

    @Id
    String mail;
    String name;
    @Setter
    String password;
}
