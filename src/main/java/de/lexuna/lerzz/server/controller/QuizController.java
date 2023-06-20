package de.lexuna.lerzz.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lexuna.lerzz.model.Card;
import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Controller class to handle quiz operations
 */
@Controller
public class QuizController {


    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private QuizService service;
    @Autowired
    private DeckService deckService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketController socketController;

    /**
     * Method to create a quiz out of a deck
     *
     * @param deckId the ID of the deck
     * @param model the Model object for the view
     * @param authentication the object representing the authenticated user
     * @return String with the name of the view "/create_quiz"
     */
    @GetMapping("deck/{deckId}/create_quiz")
    public String createQuiz(@PathVariable("deckId") String deckId, Model model, Authentication authentication) {
        String mail = authentication.getName();
        QuizService.QuizDTO quiz = service.toDTO(service.getNewQuiz(mail, deckId));
        model.addAttribute("quiz", quiz);
        model.addAttribute("deckId", deckId);
        return "/create_quiz";
    }

    @GetMapping("/join/{ownerId}")
    public String joinQuiz(@PathVariable("ownerId") String ownerId, Model model) {
        QuizService.QuizDTO quiz = service.toDTO(service.getQuiz(ownerId));
        model.addAttribute("quiz", quiz);
        return "/create_quiz";
    }

    /**
     * Method to start the quiz and return the view
     *
     * @param deckId the ID of the deck
     * @param quizId the ID of the quiz
     * @param cardDto the DTO of the cards from the deck
     * @param model
     * @param principal the object representing the authenticated user
     * @return String with the name of the view "/quiz"
     */
    @GetMapping("deck/{deckId}/quiz/{quizId}")
    public String quiz(@PathVariable("deckId") String deckId, @PathVariable("quizId") String quizId, Model model, Principal principal) {
        Quiz quiz = service.getQuiz(quizId);
        QuizService.QuizDTO quizDTO = service.toDTO(quiz);
        if(quiz.getOwner().getEmail().equals(principal.getName())) {
            service.start(quiz);
            quiz.getPlayer().stream().filter(p-> !p.getEmail().equals(principal.getName())).forEach(p -> socketController.start(p.getEmail(), deckId, quizId));
        }
        model.addAttribute("questionCount", quiz.getQuestions().size());
        model.addAttribute("quiz", quizDTO);
        return "/quiz";
    }


}
