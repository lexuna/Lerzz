package de.lexuna.lerzz.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import java.util.List;

/**
 * The Card class represents a card in a deck of cards used for a game.
 *
 * It contains fields for the card's ID, deck ID, question, answer, and type. The question and
 * answer fields can be changed by the user during the game, so setters are generated for these
 * fields using the @Setter annotation.
 *
 * The @Value annotation is used to generate the getters for the fields, as well as a constructor
 * that initializes all the fields. This creates an immutable value object that is not intended
 * to be changed after construction.
 *
 * The CardType enum is used to represent the type of the card, which can be either a question
 * or an answer.
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    private int id;
    /** The ID of the deck that this card belongs to. */
    private String deckId;
    /** The question on the front of the card. */
    private String question;
    /** The answer on the back of the card. */
    private List<String> answers;
    /** The type of the card */
    private CardType type = CardType.MULTIPLE_CHOICE;

    public Card (int id, String deckId, String question, List<String> answers) {
        this.id = id;
        this.deckId = deckId;
        this.question = question;
        this.answers = answers;
    }

}
