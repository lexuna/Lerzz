package de.lexuna.lerzz.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
public class Game {

    @Id
    private int id;
    private String name;
    private int deckId;
    @Setter
    private String description;

}
