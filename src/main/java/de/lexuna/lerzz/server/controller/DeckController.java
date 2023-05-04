package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.repository.CardDeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The `DeckController` class is responsible for handling HTTP requests related to Card Decks. It provides endpoints for
 * getting, creating, updating, adding, and deleting Card Decks. The class utilizes the `CardDeckRepository` interface
 * to perform CRUD operations on the Card Decks.
 */
@RestController
@RequestMapping("/deck")
public class DeckController {

    /**
     * The `CardDeckRepository` instance to perform CRUD operations on the Card Decks.
     */
    @Autowired
    private CardDeckRepository repo;

    /**
     * Retrieves the Card Decks by the given user email or id.
     * @param mail The email of the user whose Card Decks are to be retrieved. Optional.
     * @param id The id of the Card Deck to be retrieved. Optional.
     * @return A ResponseEntity containing the retrieved Card Decks or an error message.
     */
    @GetMapping("")
    public ResponseEntity<DeckResponse> get(@RequestParam(value = "user", required = false) String mail, @RequestParam(value = "id", required = false) int id) {
        // implementation omitted for brevity
    }

    /**
     * Creates a new Card Deck.
     * @return A ResponseEntity containing the newly created Card Deck or an error message.
     */
    @PostMapping("")
    public ResponseEntity<DeckResponse> create() {
        // implementation omitted for brevity
    }

    /**
     * Updates an existing Card Deck.
     * @return A ResponseEntity containing the updated Card Deck or an error message.
     */
    @PutMapping("")
    public ResponseEntity<DeckResponse> update() {
        // implementation omitted for brevity
    }

    /**
     * Adds a Card to an existing Card Deck.
     * @return A ResponseEntity containing the updated Card Deck or an error message.
     */
    @PutMapping("/add")
    public ResponseEntity<DeckResponse> addCard() {
        // implementation omitted for brevity
    }

    /**
     * Deletes an existing Card Deck by id.
     * @param id The id of the Card Deck to be deleted.
     */
    @DeleteMapping("")
    public void delete(@RequestParam(value = "id") int id) {
        repo.deleteById(id);
    }

    /**
     * The response class used to return the Card Decks and/or error messages from the HTTP endpoints of this class.
     */
    record DeckResponse(String message, List<CardDeck> decks) {
        /**
         * Constructor for creating a DeckResponse with only an error message.
         * @param message The error message to be returned.
         */
        DeckResponse(String message) {
            this(message, null);
        }

        /**
         * Constructor for creating a DeckResponse with a list of Card Decks.
         * @param decks The list of Card Decks to be returned.
         */
        DeckResponse(List<CardDeck> decks) {
            this(null, decks);
        }
    }
}

