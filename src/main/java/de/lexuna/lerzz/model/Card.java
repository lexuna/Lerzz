package de.lexuna.lerzz.model;

import lombok.Setter;
import lombok.Value;

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
@Value
public class Card {

    /** The ID of the card. */
    int id;

    /** The ID of the deck that this card belongs to. */
    int deckId;

    /** The question on the front of the card. */
    @Setter
    String question;

    /** The answer on the back of the card. */
    @Setter
    String answer;

    /** The type of the card */
    CardType type;
}
