package de.lexuna.lerzz.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Data class for a card deck.
 */
@Data
@Document
@Getter
@Setter
public class Deck {
    @Id
    private String id;
    private String userId; //Final
    private String name;
    private String description;
    private Instant creationTime;
    private List<Card> cards = new ArrayList<>();

    /**
     * @param userId of the author
     * @param name of the deck
     * @param description of th deck
     * @param creationTime of the deck
     */
    public Deck(final String userId,
                final String name,
                final String description,
                final Instant creationTime) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.creationTime = creationTime;
    }

    /**
     * Chooses a number of random cards of the deck
     * @param nrOfQuestions the number of cards that are needed
     *
     * @return a list of random cards from the deck
     */
    public final List<Card> getRandomQuestions(final int nrOfQuestions) {
        Random rand = new Random();
        List<Card> randomCards = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        if(this.cards.size() == nrOfQuestions) {
            return this.cards;
        }
        for (int i = 0; i < nrOfQuestions; i++) {
            int cardNr = rand.nextInt(0, this.cards.size() - 1);
            if (idSet.contains(cardNr)) {
                i--;
                continue;
            }
            idSet.add(cardNr);
            randomCards.add(this.cards.get(cardNr));
        }
        return randomCards;
    }
}
