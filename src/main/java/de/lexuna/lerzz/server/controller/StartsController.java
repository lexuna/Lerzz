package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.model.QuizMode;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StartsController {

    @Autowired
    private QuizService service;
    @Autowired
    private DeckService deckService;
    @Autowired
    private WebSocketController socketController;

    @GetMapping("/deck/{deckId}/quiz/{quizId}/stats")
    public String getStats(@PathVariable("deckId") String deckId, @PathVariable("quizId") String quizId, Principal principal, Model model) {
        Quiz quiz = service.getQuiz(quizId);
        quiz.finish(principal.getName());
        Quiz.Stats stats = quiz.getStats().get(principal.getName());
        List<Quiz.Answer> answers = quiz.getAnswers().stream().filter(a -> a.getUserId().equals(principal.getName())).collect(Collectors.toList());
        model.addAttribute("stats", stats);
        model.addAttribute("time", quiz.getTime(principal.getName()));
        model.addAttribute("answers", answers);
        model.addAttribute("deckId", deckId);
        model.addAttribute("cards", deckService.cardsAsDTOs(quiz.getQuestions()));
        if(quiz.getMode()== QuizMode.COOP) {
            quiz.getPlayer().stream().filter(p-> !p.getEmail().equals(principal.getName())).forEach(p-> socketController.end(p.getEmail()));
            return "/quiz_results";
        } else {
            model.addAttribute("times", quiz.getPlayer().stream().filter(p->quiz.getStats().get(p.getEmail()).getEnd()!=null)
                    .map(p-> quiz.getTime(p.getEmail())).collect(Collectors.toList()));
            model.addAttribute("rightAnswers", quiz.getPlayer().stream().map(p-> quiz.getStats().get(p.getEmail()).getRightAnswers()).collect(Collectors.toList()));
            model.addAttribute("users",  quiz.getPlayer().stream().filter(p->quiz.getStats().get(p.getEmail()).getEnd()!=null)
                    .map(User::getUsername).collect(Collectors.toList()));
            return "/quiz_result_users";
        }
    }
}
