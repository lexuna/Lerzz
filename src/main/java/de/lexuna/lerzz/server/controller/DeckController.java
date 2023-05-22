package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Card;
import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DeckController {

    @Autowired
    private DeckService service;

    @Autowired
    private UserService userService;

    @GetMapping("deck/{id}")
    public String getStack(@PathVariable("id") String stackId, Model model) {
        DeckService.DeckDTO deck = service.asDTO(service.getDeckById(stackId), true);
        model.addAttribute("deck", deck);
        return "deck_overview";
    }

    @PostMapping("/deck/delete/{id}")
    public String deleteStack(@PathVariable("id") String deckId, Authentication authentication) {
        service.deleteDeck(deckId);
        return "redirect:/dashboard";
    }

    @GetMapping("deck/{deckId}/add_card/{cardId}")
    public String editCard(@PathVariable("deckId") String deckId, @PathVariable("cardId") int cardId, Model model) {
        DeckService.McCardDTO card;
        if(cardId == -1) {
            card = service.getEmptyCardDTO();
        } else {
            Deck deck = service.getDeckById(deckId);
            card = service.asDTO(deck.getCards().get(cardId));
        }
        model.addAttribute("card", card);
        return "add_card";
    }

    @PostMapping("deck/{deckId}/add_card/{cardId}")
    public String addCardPost(@PathVariable("deckId") String deckId, @PathVariable("cardId") int cardId, @ModelAttribute DeckService.McCardDTO cardDto, Model model, Authentication authentication) {
        String mail = authentication.getName();
        if (cardId != -1) {
            service.editCard(deckId, cardId, cardDto);
        } else {
            User user = userService.findUserByEmail(mail);
            service.addCard(deckId, user, cardDto);
        }
        return "redirect:/deck/" + deckId;
    }

    @GetMapping("/create")
    public String create(Model model, Authentication authentication) {
        DeckService.DeckDTO deck = service.getEmptyDTO();
        String mail = authentication.getName();
        UserService.UserDTO user = userService.toDTO(userService.findUserByEmail(mail));
        model.addAttribute("username", user.getName());
        model.addAttribute(deck);
        return "create_stack";
    }

    @PostMapping("/create")
    public String createCardStack(@ModelAttribute DeckService.DeckDTO deck, Authentication authentication) {
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        service.addDeck(deck, user);
        return "redirect:/dashboard";
    }
}
