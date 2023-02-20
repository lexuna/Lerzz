package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.repository.CardDeckRepository;
import de.lexuna.lerzz.server.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("/deck")
public class DeckController {

    @Autowired
    private DeckService service;

    @GetMapping("/dashboard")
    public String home(Model model) {
        CardDeck deck = new CardDeck(0, "", "", "");
        model.addAttribute(deck);
        return "dashboard";
    }

    @ModelAttribute("allDecks")
    public List<CardDeck> populateSeedStarters() {
        return this.service.findAll();
    }

    @RequestMapping("/deckmng")
    public String showDecks(final CardDeck seedStarter) {
        return "deckmng";
    }

}
