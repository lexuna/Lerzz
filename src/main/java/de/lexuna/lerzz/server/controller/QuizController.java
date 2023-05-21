package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import de.lexuna.lerzz.server.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("quiz")
public class QuizController {

    @Autowired
    private QuizService service;
    @Autowired
    private DeckService decService;

    @Autowired
    private UserService userService;

    @GetMapping("deck/{id}/create_quiz")
    public String create_quiz(@PathVariable("id") String deckId, Model model, Authentication authentication) {
        String mail = authentication.getName();
        QuizService.QuizDTO quiz = service.toDTO(service.getNewQuiz(mail, deckId));
        model.addAttribute("quiz", quiz);
        return "/create_quiz";
    }

    @GetMapping("/deck/{deckId}/quiz")
    public String quiz(@PathVariable("deckId") String deckId, @ModelAttribute QuizService.QuizDTO quiz, Model model) {
//        model.addAttribute("quiz", quiz);
        return "/quiz";
    }

}
