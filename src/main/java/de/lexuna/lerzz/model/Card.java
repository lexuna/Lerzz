package de.lexuna.lerzz.model;

import lombok.Setter;
import lombok.Value;

@Value
public class Card {

    int id;
    int deckId;
    @Setter
    String question;
    @Setter
    String answer;
    CardType type;

}
