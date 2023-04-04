package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Card;
import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.CardType;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DeckController {

    @Autowired
    private DeckService service;

    @Autowired
    private UserService userService;

    @GetMapping("deck/{id}")
    public String getStack(@PathVariable("id") String stackId, Model model, Authentication authentication) {
        CardDeck deck = service.getDeckDyId(stackId);
        model.addAttribute("deck", deck);
        return "stack_preview";
    }

    @PostMapping("/deck/delete/{id}")
    public String deleteStack(@PathVariable("id") String deckId,Model model) {
        service.deleteDeck(deckId);
        return "redirect:/dashboard";
    }

    @GetMapping("deck/add_card/{id}")
    public String addCard(@PathVariable("id") String stackId, Model model, Authentication authentication) {
        CardDTO card = new CardDTO(stackId, "", "", "", "", "");
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("deckId", stackId);
        model.addAttribute(card);
        return "add_card";
    }

    @PostMapping("deck/add_card/{id}")
    public String addCardPost(@PathVariable("id") String stackId, @ModelAttribute CardDTO cardDto, Model model, Authentication authentication) {
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        service.addCard(stackId, user, cardDto);
        return "redirect:/deck/"+stackId;
    }

    @GetMapping("/create")
    public String createCardStackHome(Model model, Authentication authentication) {
        CardDeck deck = new CardDeck("", "", "", null);
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        model.addAttribute("username", user.getUsername());
        model.addAttribute(deck);
        return "create_stack";
    }

    @PostMapping(value = "/create")
    public String createCardStack(@ModelAttribute CardDeck deck, Model model, Authentication authentication) {
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        service.addDeck(new CardDeck(user.getUsername(), deck.getName(), deck.getDescription(), Instant.now()));
        return "redirect:/dashboard";
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public class CardDTO {
        String deckId;
        String question;
        String answer1;
        String answer2;
        String answer3;
        String answer4;
    }
}
