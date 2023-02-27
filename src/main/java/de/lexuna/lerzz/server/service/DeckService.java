package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.repository.CardDeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
