package de.lexuna.lerzz.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class for a multiple choice card witch extends a card
 */
@Getter
@Setter
public class McCard extends Card {

    private List<String> answers;

    /**
     * Constructor for a new multiple choice card with the properties as input
     *
     * @param id the ID of the card
     * @param deckId the ID of the deck where the card belongs
     * @param question the question of the card
     * @param author the author of the card
     * @param answers the list of possible answers
     * @param rightAnswer the correct answer
     */
    public McCard(final int id,
                  final String deckId,
                  final String question,
                  final String author,
                  final List<String> answers,
                  final String rightAnswer) {
        super(id, deckId, question,
                author, rightAnswer, CardType.MULTIPLE_CHOICE);
        this.answers = answers;
    }
}
