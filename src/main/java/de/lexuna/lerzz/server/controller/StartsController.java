package de.lexuna.lerzz.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.model.QuizMode;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller class to initiate a quiz with a method to return the quiz statistics
 */
@Controller
public class StartsController {

    @Autowired
    private QuizService service;
    @Autowired
    private DeckService deckService;
    @Autowired
    private WebSocketController socketController;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Method to return the quiz statistics
     *
     * @param deckId the ID of the deck with the questions of the quiz
     * @param quizId the ID of the quiz
     * @param principal the principal object representing the current user
     * @param model the model object with the data for the view
     * @return the view name for displaying the quiz results
     */
    @GetMapping("/deck/{deckId}/quiz/{quizId}/stats")
    public String getStats(@PathVariable("deckId") String deckId, @PathVariable("quizId") String quizId, Principal principal, Model model) throws JsonProcessingException {
        Quiz quiz = service.getQuiz(quizId);
        quiz.finish(principal.getName());
//        Quiz.Stats stats = quiz.getStats().get(principal.getName());
        if (quiz.getMode() == QuizMode.COOP) {
            String email = quiz.getOwner().getEmail();
            List<Quiz.Answer> answers = quiz.getAnswers().stream().filter(a -> a.getUserId().equals(email)).collect(Collectors.toList());
            Quiz.Stats stats = quiz.getStats().get(email);
            model.addAttribute("stats", stats);
            model.addAttribute("time", quiz.getTime(email));
            model.addAttribute("answers", answers);
            model.addAttribute("deckId", deckId);
            model.addAttribute("cards", deckService.cardsAsDTOs(quiz.getQuestions()));
            quiz.getPlayer().stream().filter(p -> !p.getEmail().equals(principal.getName())).forEach(p -> socketController.end(p.getEmail()));
            return "/quiz_results";
        } else {
            List<String> times = new ArrayList<>();
            List<Integer> rightAnswers = new ArrayList<>();
            List<String> users = new ArrayList<>();
            for (User player : quiz.getPlayer()) {
                if (quiz.getStats().get(player.getEmail()).getEnd() != null) {
                    times.add(quiz.getTime(player.getEmail()));
                    rightAnswers.add(quiz.getStats().get(player.getEmail()).getRightAnswers());
                    users.add(player.getUsername());
                    if(!player.getEmail().equals(principal.getName())) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("time", quiz.getTime(principal.getName()));
                        map.put("rightAnswer", quiz.getStats().get(principal.getName()).getRightAnswers());
                        map.put("user", userService.findUserByEmail(principal.getName()).getUsername());
                        map.put("nr", quiz.getStats().values().stream().filter(s-> s.getEnd()!=null).count());
                        socketController.addStats(player.getEmail(), objectMapper.writeValueAsString(map));
                    }
                }
            }
            model.addAttribute("times", times);
            model.addAttribute("rightAnswers", rightAnswers);
            model.addAttribute("users", users);
            return "/quiz_result_users";
        }
    }

    @GetMapping("/deck/{deckId}/quiz/{quizId}/stats/details")
    public String getDetails(@PathVariable("deckId") String deckId, @PathVariable("quizId") String quizId, Principal principal, Model model) {
        Quiz quiz = service.getQuiz(quizId);
        quiz.finish(principal.getName());
        Quiz.Stats stats = quiz.getStats().get(principal.getName());
        List<Quiz.Answer> answers = quiz.getAnswers().stream().filter(a -> a.getUserId().equals(principal.getName())).collect(Collectors.toList());
        model.addAttribute("stats", stats);
        model.addAttribute("time", quiz.getTime(principal.getName()));
        model.addAttribute("answers", answers);
        model.addAttribute("deckId", deckId);
        model.addAttribute("cards", deckService.cardsAsDTOs(quiz.getQuestions()));
//        quiz.getPlayer().stream().filter(p -> !p.getEmail().equals(principal.getName())).forEach(p -> socketController.end(p.getEmail()));
        return "/quiz_results";
    }
}
