package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * The `DeckController` class is responsible for handling HTTP requests related to Card Decks. It provides endpoints for
 * getting, creating, updating, adding, and deleting Card Decks. The class utilizes the `CardDeckRepository` interface
 * to perform CRUD operations on the Card Decks.
 */
@Controller
public class DeckController {

    @Autowired
    private DeckService service;

    @Autowired
    private UserService userService;

    /**
     * controller to get to a specific deck
     * @param stackId String
     * @param model Model
     * @return deck_overview
     */
    @GetMapping("deck/{id}")
    public final String getDeck(@PathVariable("id") final String stackId, final Model model) {
        DeckService.DeckDTO deck = service.asDTO(service.getDeckById(stackId), true);
        model.addAttribute("deck", deck);
        return "deck_overview";
    }

    /**
     * controller to delete to a specific deck
     * @param deckId String
     * @return redirect:/dashboard
     */
    @PostMapping("/deck/delete/{id}")
    public final String deleteDeck(@PathVariable("id") final String deckId) {
        service.deleteDeck(deckId);
        return "redirect:/dashboard";
    }

    /**
     * controller to edit a card
     * @param deckId  String
     * @param cardId int
     * @param model Model
     * @return add_card
     */
    @GetMapping("deck/{deckId}/add_card/{cardId}")
    public final String editCard(@PathVariable("deckId") final String deckId, @PathVariable("cardId") final int cardId,
                                 final Model model) {
        DeckService.McCardDTO card;
        if (cardId == -1) {
            card = service.getEmptyCardDTO();
        } else {
            Deck deck = service.getDeckById(deckId);
            card = service.asDTO(deck.getCards().get(cardId));
        }
        model.addAttribute("card", card);
        return "add_card";
    }

    /**
     * Controller to add a card
     * @param deckId String
     * @param cardId int
     * @param cardDto McCardDTO
     * @param authentication Authentication
     * @return "redirect:/deck/" + deckId
     */
    @PostMapping("deck/{deckId}/add_card/{cardId}")
    public final String addCard(@PathVariable("deckId") final String deckId,
                                    @PathVariable("cardId") final int cardId,
                                    @ModelAttribute final DeckService.McCardDTO cardDto,
                                    final Authentication authentication) {
        String mail = authentication.getName();
        if (cardId != -1) {
            service.editCard(deckId, cardId, cardDto);
        } else {
            User user = userService.findUserByEmail(mail);
            service.addCard(deckId, user, cardDto);
        }
        return "redirect:/deck/" + deckId;
    }

    /**
     * Controller to get the create a deck page
     * @param model Model
     * @param authentication Authentication
     * @return create_stack
     */
    @GetMapping("/create")
    public final String create(final Model model, final Authentication authentication) {
        DeckService.DeckDTO deck = service.getEmptyDTO();
        String mail = authentication.getName();
        UserService.UserDTO user = userService.toDTO(userService.findUserByEmail(mail));
        model.addAttribute("username", user.getName());
        model.addAttribute(deck);
        return "create_stack";
    }

    /**
     * Controller to create a deck
     * @param deck DeckDTO
     * @param authentication Authentication
     * @return redirect:/dashboard
     */
    @PostMapping("/create")
    public final String createDeck(@ModelAttribute final DeckService.DeckDTO deck,
                                        final Authentication authentication) {
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        service.addDeck(deck, user);
        return "redirect:/dashboard";
    }
}
