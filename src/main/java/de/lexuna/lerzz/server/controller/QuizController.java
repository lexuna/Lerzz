package de.lexuna.lerzz.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

/**
 * Controller class to handle quiz operations
 */
@Controller
public class QuizController {

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
     * @param deckId         the ID of the deck
     * @param model          the Model object for the view
     * @param authentication the object representing the authenticated user
     * @return String with the name of the view "/create_quiz"
     */
    @GetMapping("deck/{deckId}/create_quiz")
    public final String createQuiz(@PathVariable("deckId") final String deckId, final Model model,
                                   final Authentication authentication) {
        String mail = authentication.getName();
        QuizService.QuizDTO quiz = service.toDTO(service.getNewQuiz(mail, deckId));
        model.addAttribute("quiz", quiz);
        model.addAttribute("deckId", deckId);
        return "/create_quiz";
    }

    /**
     * controller for join a quiz
     * @param ownerId String
     * @param model Model
     * @return create_quiz
     */
    @GetMapping("/join/{ownerId}")
    public final String joinQuiz(@PathVariable("ownerId") final String ownerId, final Model model,
                                 final Principal principal) {
        Quiz quiz = service.getQuiz(ownerId);
        quiz.getPlayer().add(userService.findUserByEmail(principal.getName()));
        QuizService.QuizDTO quizDTO = service.toDTO(quiz);
        model.addAttribute("quiz", quizDTO);
        return "/create_quiz";
    }

    /**
     * Method to start the quiz and return the view
     *
     * @param deckId    the ID of the deck
     * @param quizId    the ID of the quiz
     * @param model     Model
     * @param principal the object representing the authenticated user
     * @return String with the name of the view "/quiz"
     */
    @GetMapping("deck/{deckId}/quiz/{quizId}")
    public final String quiz(@PathVariable("deckId") final String deckId, @PathVariable("quizId") final String quizId,
                             final Model model, final Principal principal) {
        Quiz quiz = service.getQuiz(quizId);
        QuizService.QuizDTO quizDTO = service.toDTO(quiz);
        if (quiz.getOwner().getEmail().equals(principal.getName())) {
            service.start(quiz);
            quiz.getPlayer().stream().filter(p -> !p.getEmail().equals(principal.getName()))
                    .forEach(p -> socketController.start(p.getEmail(), deckId, quizId));
        }
        model.addAttribute("questionCount", quiz.getQuestions().size());
        model.addAttribute("quiz", quizDTO);
        return "/quiz";
    }


}
