package de.lexuna.lerzz.model;

import lombok.Setter;
import lombok.Value;

@Value
public class Game {

    int id;
    @Setter
    String name;
    int deckId;
    @Setter
    String description;

}
