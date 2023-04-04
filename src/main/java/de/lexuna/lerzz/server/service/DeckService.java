package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.Card;
import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.CardDeckRepository;
import de.lexuna.lerzz.server.controller.DeckController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckService {

    @Autowired
    private CardDeckRepository repo;

    public List<CardDeck> findAll() {
        return repo.findAll();
    }

    public void addDeck(CardDeck deck) {
        repo.insert(deck);
    }

    public void deleteDeck(String deckId) {
        repo.deleteById(deckId);
    }

    public CardDeck getDeckDyId(String stackId) {
        return repo.findById(stackId).get();
    }

    public void addCard(String stackId, User user, DeckController.CardDTO cardDTO) {
        CardDeck deck = getDeckDyId(stackId);
        int cardId = deck.getCards().size();
        List<String> answers = new ArrayList<>();
        answers.add(cardDTO.getAnswer1());
        answers.add(cardDTO.getAnswer2());
        answers.add(cardDTO.getAnswer3());
        answers.add(cardDTO.getAnswer4());
        deck.getCards().add(new Card(cardId, deck.getId(), cardDTO.getQuestion(), answers));
        repo.save(deck);
    }
}
