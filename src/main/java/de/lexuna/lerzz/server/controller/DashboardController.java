package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class to handle dashboard related operations
 */
@Controller
public class DashboardController {

    @Autowired
    private DeckService service;

    @Autowired
    private UserService userService;

    /**
     * Method to return the dashboard view
     *
     * @param model the model object
     * @param authentication the object representing the authenticated user
     * @return the String with the name of the view "dashboard"
     */
    @GetMapping("/dashboard")
    public String home(Model model, Authentication authentication) {
        DeckService.DeckDTO deck = service.getEmptyDTO();
        model.addAttribute(deck);
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        model.addAttribute("username", user.getUsername());
        return "dashboard";
    }

    /**
     * Method to delete a deck
     *
     * @param deckId the ID of the deck that should be deleted
     * @param model the model object
     * @param authentication the object representing the authenticated user
     * @return the String with the name of the view "dashboard"
     */
    @DeleteMapping("delete/{id}")
    public String deleteCardStack(@PathVariable("id") String deckId, Model model, Authentication authentication) {
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        service.deleteDeck(deckId);
        return "dashboard";
    }

    /**
     *  Method to get all deck DTOs
     *
     * @return a list of all DTO objects
     */
    @ModelAttribute("allDecks")
    public List<DeckService.DeckDTO> getAllDecks() {
        return service.asDTOs(this.service.findAll());
    }

}
