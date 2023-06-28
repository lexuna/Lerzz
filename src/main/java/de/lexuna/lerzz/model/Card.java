package de.lexuna.lerzz.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

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
public abstract class Card {

    @Id
    private int id;
    private String deckId;
    private String author;
    private String question;
    private String rightAnswer;
    private CardType type = CardType.MULTIPLE_CHOICE;

    /**
     * Checks if the given answer is correct.
     * @param answerText the text of the given answer
     * @return true if the given answer is correct
     */
    public boolean checkAnswer(String answerText) {
        return rightAnswer.equals(answerText);
    }
}
