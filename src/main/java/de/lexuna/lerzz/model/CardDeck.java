package de.lexuna.lerzz.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The CardDeck class represents a deck of cards used for a game.
 *
 * It contains fields for the deck's ID, user ID, name, and description. The name and description
 * fields can be changed by the user during the game, so the @NonFinal and @Setter annotations are
 * used to indicate that these fields are mutable and to generate setters for them.
 *
 * The @Value annotation is used to generate the getters for the fields, as well as a constructor
 * that initializes all the fields. This creates an immutable value object that is not intended
 * to be changed after construction.
 */
@Data
@Document
@Getter
@Setter
public class CardDeck {

    /** The ID of the deck. */
    @Id
    private String id;
    /** The ID of the user who owns the deck. */
    private String userId; //Final
    /** The name of the deck. */
    private String name;
    /** The description of the deck. */
    private String description;
    private Instant creationTime;
    private List<Card> cards = new ArrayList<>();

    public CardDeck(String userId, String name, String description, Instant creationTime) {
        this.userId = userId;
        this.name=name;
        this.description = description;
        this.creationTime = creationTime;
    }

}
