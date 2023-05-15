package de.lexuna.lerzz.model;

import lombok.Getter;

import java.util.List;

@Getter
public class McCard extends Card {

    private List<String> answers;

    public McCard(int id, String deckId, String question, String author, List<String> answers, String rightAnswer) {
        super(id, deckId, question, author, rightAnswer);
        this.answers = answers;
    }
}
