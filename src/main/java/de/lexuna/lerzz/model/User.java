package de.lexuna.lerzz.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;

@Value
public class User {

    @Id
    String mail;
    @NonFinal
    @Setter
    String name;
    @NonFinal
    @Setter
    String password;
}
