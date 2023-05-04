package de.lexuna.lerzz.model;

import lombok.Setter;
import lombok.Value;

/**
 * The Game class represents a game in the application. A game is associated with a deck of cards and has a name and description.
 *
 * This class is an immutable value object. Once an instance of the class is created, its values cannot be modified.
 * To change the values of a Game instance, a new instance with the updated values must be created.
 */
@Value
public class Game {

    /** The ID of the game. */
    int id;

    /** The name of the game. */
    @Setter
    String name;

    /** The ID of the deck associated with the game. */
    int deckId;

    /** The description of the game. */
    @Setter
    String description;

}

