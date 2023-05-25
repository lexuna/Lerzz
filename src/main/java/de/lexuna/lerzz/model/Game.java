package de.lexuna.lerzz.model;

import lombok.Data;
import lombok.Getter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Id;

/**
 * The Game class represents a game in the application. A game is associated with a deck of cards and has a name and description.
 *
 * This class is an immutable value object. Once an instance of the class is created, its values cannot be modified.
 * To change the values of a Game instance, a new instance with the updated values must be created.
 */
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
