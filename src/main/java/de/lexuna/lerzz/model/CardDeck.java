package de.lexuna.lerzz.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
public class CardDeck {

    int id;
    String userId;
    @NonFinal
    @Setter
    String name;
    @NonFinal
    @Setter
    String description;

}
