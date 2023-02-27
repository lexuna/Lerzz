package de.lexuna.lerzz.model;

import lombok.Data;
import lombok.Generated;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class CardDeck {

    @Id
    String id;
    private final String userId;
    @NonFinal
    @Setter
    String name;
    @NonFinal
    @Setter
    String description;

    private final Instant creationTime;

    List<Card> cards = new ArrayList<>();

    public CardDeck(String userId, String name, String description, Instant creationTime) {
        this.userId = userId;
        this.name=name;
        this.description = description;
        this.creationTime = creationTime;
    }

}
