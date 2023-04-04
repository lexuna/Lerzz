package de.lexuna.lerzz.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    private int id;
    private String deckId;
    private String question;
    private List<String> answers;
    private CardType type = CardType.MULTIPLE_CHOICE;

    public Card (int id, String deckId, String question, List<String> answers) {
        this.id = id;
        this.deckId = deckId;
        this.question = question;
        this.answers = answers;
    }

}
