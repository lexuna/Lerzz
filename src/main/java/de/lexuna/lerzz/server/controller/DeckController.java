package de.lexuna.lerzz.server.controller;

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
        DeckService.DeckDTO deck = service.asDTO(service.getDeckDyId(stackId), true);
        model.addAttribute("deck", deck);
        return "deck_overview";
    }

    @PostMapping("/deck/delete/{id}")
    public String deleteStack(@PathVariable("id") String deckId, Authentication authentication) {
        service.deleteDeck(deckId);
        return "redirect:/dashboard";
    }

    @GetMapping("deck/add_card/{id}")
    public String addCard(@PathVariable("id") String deckId, Model model, Authentication authentication) {
        DeckService.McCardDTO card = service.getEmptyCardDTO();
        String mail = authentication.getName();
        UserService.UserDTO user = userService.toDTO(userService.findUserByEmail(mail));
        model.addAttribute(user);
        model.addAttribute("deckId", deckId);
        model.addAttribute("card", card);
        return "add_card";
    }

    @PostMapping("deck/add_card/{deckId}")
    public String addCardPost(@PathVariable("deckId") String deckId, @ModelAttribute DeckService.McCardDTO cardDto, Model model, Authentication authentication) {
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        service.addCard(user, cardDto);
        return "redirect:/deck/"+deckId;
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
