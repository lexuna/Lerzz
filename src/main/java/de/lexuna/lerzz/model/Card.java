package de.lexuna.lerzz.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

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

    public Card(int id, String deckId, String question, String author, String rightAnswer) {
        this.id = id;
        this.deckId = deckId;
        this.question = question;
        this.author = author;
        this.rightAnswer = rightAnswer;
    }

    public boolean checkAnswer(String answerText) {
        return rightAnswer.equals(answerText);
    }
}
