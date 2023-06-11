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
//@SessionAttributes({"quiz"})
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

//    @PostMapping("deck/{deckId}/create_quiz/invite")
//    public String invite(@PathVariable("deckId") String deckId, @ModelAttribute QuizService.QuizDTO quiz, @ModelAttribute String username, Model model, Authentication authentication) {
////        String username = (String) model.getAttribute("username");
//        quiz.getInvited().add(userService.toDTO(userService.findUserByName(username)));
////        model.addAttribute("quiz", quiz);
//        return "create_quiz";
//    }

//    @PostMapping("deck/{deckId}/quiz/{quizId}")
//    public String quiz(@PathVariable("deckId") String deckId, Model model) {
//        QuizService.QuizDTO quiz = (QuizService.QuizDTO) model.getAttribute("quiz");
////        DeckService.McCardDTO card;
//        DeckService.McCardDTO cardDto = deckService.asDTO(service.start(quiz), quiz.getOwnerId());
//        model.addAttribute("questionCount", service.getQuestionCount(quiz));
//        model.addAttribute("card", cardDto);
//        quiz.setStarted(true);
//        socketController.updatePositions(service.getQuiz(cardDto).getPositions(), quiz.getOwnerId());
//        return "redirect:/quiz/" + quiz.getId();
//    }

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
    @PostMapping("deck/{deckId}/quiz/{quizId}")
    public String quiz(@PathVariable("deckId") String deckId, @PathVariable("quizId") String quizId, @ModelAttribute DeckService.McCardDTO cardDto, Model model, Principal principal) {
//        QuizService.QuizDTO quiz = (QuizService.QuizDTO) model.getAttribute("quiz");
//        DeckService.McCardDTO card;
        QuizService.QuizDTO quizDTO = service.toDTO(service.getQuiz(quizId));
        Quiz quiz = service.getQuiz(quizId);
//        if (quizDTO.isStarted()) {
//            cardDto = service.next(userService.findUserByEmail(principal.getName()), cardDto, deckId, quizId);
//            model.addAttribute("questionCount", service.getQuestionCount(quizDTO));
//            model.addAttribute("card", cardDto);
//            model.addAttribute("quiz", quizDTO);
//        } else {
            cardDto = deckService.asDTO(service.start(quizDTO), quizDTO.getOwnerId());
            model.addAttribute("questionCount", quiz.getQuestions().size());
//            model.addAttribute("card", cardDto);
            model.addAttribute("quiz", quizDTO);
//            model.addAttribute("questionNr", 1);
//            quizDTO.setStarted(true);
//        }
//        socketController.updatePositions(quiz.getPositions(), quizDTO.getOwnerId());
        return "/quiz";
    }

//    @PostMapping("/next")
//    public DeckService.McCardDTO next(@RequestBody String payload, Model model, Principal principal, @Header("deckId") String deckId) throws JsonProcessingException {
//        DeckService.McCardDTO card = objectMapper.readValue(payload, DeckService.McCardDTO.class);
//        DeckService.McCardDTO next = service.next(userService.findUserByEmail(principal.getName()), card, deckId);
////        messagingTemplate.convertAndSendToUser(principal.getName(), "/secured/user/queue", objectMapper.writeValueAsString(next));
////        messagingTemplate.convertAndSend( "/topic/next", next);
//        Quiz quiz = service.updatePosition(principal.getName(), card);
//        quiz.getPlayer().forEach(p -> socketController.updatePositions(quiz.getPositions(), quiz.getOwner().getId()));
//        model.addAttribute("card", next);
//        return next;
//    }

//    @PostMapping("/next")
//    @ResponseBody
//    public String next(@RequestBody String payload) throws JsonProcessingException {
//        Map<String, Object> map = objectMapper.readValue(payload, Map.class);
//        String deckId = (String) map.get("deckId");
//        String quizId = (String) map.get("quizId");
//        int cardId = (int) map.get("cardId");
//        int questionNr = Integer.parseInt((String) map.get("questionNr"));
//        int solution = (int) map.get("solution");
//
//        DeckService.McCardDTO card = deckService.asDTO(service.getQuiz(quizId).getQuestions().get(questionNr));
////        model.addAttribute("card", card);
//        return objectMapper.writeValueAsString(card);
//    }
}
