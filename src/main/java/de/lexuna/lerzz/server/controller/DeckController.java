package de.lexuna.lerzz.server.controller;

import com.mongodb.MongoBulkWriteException;
import de.lexuna.lerzz.modle.CardDeck;
import de.lexuna.lerzz.modle.User;
import de.lexuna.lerzz.modle.repository.CardDeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    }

    @PutMapping("")
    public ResponseEntity<DeckResponse> update() {//ToDo erstellen
    }

    @PutMapping("/add")
    public ResponseEntity<DeckResponse> addCard() {//ToDo erstellen
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
