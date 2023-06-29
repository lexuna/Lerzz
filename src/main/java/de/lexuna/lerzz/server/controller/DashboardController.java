package de.lexuna.lerzz.server.controller;

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
    public final String home(final Model model,
                             final Authentication authentication) {
        DeckService.DeckDTO deck = service.getEmptyDTO();
        model.addAttribute(deck);
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        model.addAttribute("username", user.getUsername());
        return "dashboard";
    }

    /**
     * Method to return the dashboard view
     *
     * @param model the model object
     * @param authentication the object representing the authenticated user
     * @return the String with the name of the view "dashboard"
     */
    @PostMapping("/dashboard")
    public final String getDashboard(final Model model,
                             final Authentication authentication) {
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
     * @param authentication the object representing the authenticated user
     * @return the String with the name of the view "dashboard"
     */
    @DeleteMapping("delete/{id}")
    public final String deleteCardStack(@PathVariable("id") final String deckId,
                                        final Authentication authentication) {
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        service.deleteDeck(deckId);
        return "dashboard";
    }

    /**
     *  Method to get all deck DTOs created by that user
     *
     * @return a list of all DTO objects
     */
    @ModelAttribute("userDecks")
    public final List<DeckService.DeckDTO> getUserDecks(
            final Authentication authentication) {
        return service.asDTOs(this.service.getUserDecks(authentication.getName()));
    }

    /**
     *  Method to get all deck DTOs that are not created by thar user
     *
     * @return a list of all DTO objects
     */
    @ModelAttribute("communityDecks")
    public final List<DeckService.DeckDTO> getCommunityDecks(
            final Authentication authentication) {
        return service.asDTOs(this.service.getCommunityDecks(authentication.getName()));
    }

}
