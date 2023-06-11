package de.lexuna.lerzz.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lexuna.lerzz.model.Card;
import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.QuizService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class QuizRestController {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private QuizService service;
    @Autowired
    private DeckService deckService;

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketController socketController;

    @PostMapping("/invite")
    public void invite(@RequestBody String payload, Principal principal) throws JsonProcessingException {
        Quiz quiz = service.getQuiz(userService.findUserByEmail(principal.getName()).getId());
        Map<String, String> map= new HashMap<>();
        map.put("quiz", quiz.getDeck().getName());
        map.put("owner", principal.getName());
        map.put("quizId", quiz.getId());
        socketController.invite(objectMapper.writeValueAsString(map), userService.findUserByName(payload).getEmail());
    }

    @PostMapping("/invitedPlayer")
    @ResponseBody
    public String getInvited(@RequestBody String payload, Principal principal) throws JsonProcessingException {
        boolean invited = Boolean.parseBoolean(payload);
        Quiz quiz;
        if(invited) {
            quiz = service.getQuizForInvited(userService.findUserByEmail(principal.getName()));
        } else {
            quiz = service.getQuiz(userService.findUserByEmail(principal.getName()).getId());
        }
        List<String> collect = quiz.getPlayer().stream().map(p -> p.getUsername()).collect(Collectors.toList());
        return objectMapper.writeValueAsString(collect);
    }

    @PostMapping("/invitationAccepted")
    public void invitationAccepted(@RequestBody String payload, Principal principal) {
        Quiz quiz = service.getQuiz(userService.findUserByEmail(payload).getId());
        quiz.getPlayer().add(userService.findUserByEmail(principal.getName()));
//        QuizService.QuizDTO quiz = service.toDTO(quiz1);
//        quiz.getInvited().add(userService.toDTO(userService.findUserByEmail(principal.getName())));
//        Map<String, String> map= new HashMap<>();
//        map.put("invited", objectMapper.writeValueAsString(quiz.getInvited()));
//        map.put("owner", principal.getName());
        socketController.invitationAccepted(userService.findUserByEmail(principal.getName()).getUsername(), payload);
    }

    @PostMapping("/card")
    @ResponseBody
    public String startQuiz(@RequestBody String payload) throws JsonProcessingException {
        String quizId = (payload.substring(payload.lastIndexOf("/") + 1)).replace("}", "").replace("\"", "");
        Quiz quiz = service.getQuiz(quizId);
        QuizService.QuizDTO quizDTO = service.toDTO(quiz);
        DeckService.McCardDTO cardDto = deckService.asDTO(quiz.getQuestions().get(0));
        quizDTO.setStarted(true);
        Map<String, Object> map = new HashMap<>();
        map.put("card", cardDto);
        map.put("positions", quiz.getPositions());
        return objectMapper.writeValueAsString(map);
    }

    @PostMapping("/next")
    @ResponseBody
    public String next(@RequestBody String payload, Principal principal) throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(payload, Map.class);
        String deckId = (String) map.get("deckId");
        String quizId = (String) map.get("quizId");
        int cardId = (int) map.get("cardId");
//        int questionNr = Integer.parseInt((String) map.get("questionNr"));
        int solution = (int) map.get("solution");
        Quiz quiz = service.getQuiz(quizId);

        DeckService.McCardDTO card = service.next(userService.findUserByEmail(principal.getName()), cardId, solution, quiz);

//        DeckService.McCardDTO card = deckService.asDTO(service.getQuiz(quizId).getQuestions().get(questionNr));
        Map<String, Object> response = new HashMap<>();
        response.put("card", card);
        response.put("lastCard", quiz.isLastCard(card));
        response.put("positions", quiz.getPositions());
//        response.put("questionNr", questionNr+1);
//        socketController.updatePositions(quiz.getPositions(), quiz.getId());
        return objectMapper.writeValueAsString(response);
    }

    @PostMapping("/end")
    @ResponseBody
    public String end(@RequestBody String payload, Principal principal) throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(payload, Map.class);
        String quizId = (String) map.get("quizId");
        int cardId = (int) map.get("cardId");
        int solution = (int) map.get("solution");
        Quiz quiz = service.getQuiz(quizId);
        service.end(userService.findUserByEmail(principal.getName()), cardId, solution, quiz);
        return "";
    }
}
