package de.lexuna.lerzz.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

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
@Value
public class CardDeck {

    /** The ID of the deck. */
    int id;

    /** The ID of the user who owns the deck. */
    String userId;

    /** The name of the deck. */
    @NonFinal
    @Setter
    String name;

    /** The description of the deck. */
    @NonFinal
    @Setter
    String description;

}
