package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.repository.CardDeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/deck")
public class DeckController {

    @Autowired
    private CardDeckRepository repo;

    @GetMapping("")
    public ResponseEntity<DeckResponse> get(@RequestParam(value = "user", required = false) String mail, @RequestParam(value = "id", required = false) int id) {
        List<CardDeck> decks = new ArrayList<>();
        if (mail != null && !mail.isBlank()) {
            decks.addAll(repo.findByUserId(mail));
        }
        if (id != 0) {
            decks.add(repo.findById(id).get()); //TODO auf empty prüfen
        }
        return ResponseEntity.ok(new DeckResponse(decks));
    }

    @PostMapping("")
    public ResponseEntity<DeckResponse> create() { //ToDo erstellen
        return null;
    }

    @PutMapping("")
    public ResponseEntity<DeckResponse> update() {//ToDo erstellen
        return null;
    }

    @PutMapping("/add")
    public ResponseEntity<DeckResponse> addCard() {//ToDo erstellen
        return null;
    }

    @DeleteMapping("")
    public void delete(@RequestParam(value = "id") int id) {
        repo.deleteById(id);
    }

    record DeckResponse(String message, List<CardDeck> decks) {
        DeckResponse(String message) {
            this(message, null);
        }

        DeckResponse(List<CardDeck> decks) {
            this(null, decks);
        }
    }
}
