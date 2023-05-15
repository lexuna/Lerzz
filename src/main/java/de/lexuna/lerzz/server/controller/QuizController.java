package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuizController {

    @Autowired
    private QuizService service;
    @Autowired
    private DeckService decService;

    @Autowired
    private UserService userService;

    @GetMapping("deck/{id}/create_quiz")
    public String create_quiz(@PathVariable("id") String deckId, Model model, Authentication authentication) {
        QuizService.QuizDTO quiz = service.getEmptyDTO();
        String mail = authentication.getName();
        User user = userService.findUserByEmail(mail);
        Deck deck = decService.getDeckDyId(deckId);
        quiz.setDeckName(deck.getName());
        quiz.setOwnerId(user.getId());
        quiz.getInvited().add(userService.toDTO(user));
        model.addAttribute("quiz", quiz);
        model.addAttribute("deckId", deckId);
        model.addAttribute("username", "blub");
//        model.addAttribute("deck", deck);
//        model.addAttribute("owner", user);
        return "/create_quiz";
    }

    @PostMapping("deck/{id}/create_quiz/invite")
    public String invite(@PathVariable("id") String deckId, @ModelAttribute QuizService.QuizDTO quiz, @ModelAttribute String username, Authentication authentication) {
//        String username = (String) model.getAttribute("username");
        quiz.getInvited().add(userService.toDTO(userService.findUserByName(username)));
        return "create_quiz";
    }

}
