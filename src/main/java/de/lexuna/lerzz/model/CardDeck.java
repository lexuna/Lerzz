package de.lexuna.lerzz.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
@Getter
@Setter
public class CardDeck {
    @Id
    private String id;
    private String userId; //Final
    private String name;
    private String description;
    private Instant creationTime;
    private List<Card> cards = new ArrayList<>();

    public CardDeck(String userId, String name, String description, Instant creationTime) {
        this.userId = userId;
        this.name=name;
        this.description = description;
        this.creationTime = creationTime;
    }

}
