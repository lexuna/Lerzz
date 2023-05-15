package de.lexuna.lerzz.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

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

    public Deck(String userId, String name, String description, Instant creationTime) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.creationTime = creationTime;
    }

    public List<Card> getRandomQuestions(int nrOfQuestions) {
        Random rand = new Random();
        List<Card> cards = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        for (int i = 0; i < nrOfQuestions; i++) {
            int cardNr = rand.nextInt(0, this.cards.size() - 1);
            if (idSet.contains(cardNr)) {
                i--;
                continue;
            }
            cards.add(this.cards.get(cardNr));
        }
        return cards;
    }
}
