package de.lexuna.lerzz.modle;

import lombok.Setter;
import lombok.Value;

@Value
public class CardDeck {

    int id;
    @Setter
    String name;
    @Setter
    String description;
}
