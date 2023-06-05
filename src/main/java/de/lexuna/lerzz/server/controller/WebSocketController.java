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
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class WebSocketController {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DeckService deckService;
    @Autowired
    private UserService userService;
//    @Autowired
//    private QuizService quizService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/editDeck")
    public String editDeck(@Payload String payload) throws JsonProcessingException {
        System.out.println("Nachricht empfangen: " + payload);
        DeckService.DeckDTO deck = objectMapper.readValue(payload, DeckService.DeckDTO.class);
        deckService.update(deck);
        return "Nachricht empfangen: " + payload;
    }

    @MessageMapping("/invite")
    public String invite(@Payload String payload, Authentication authentication) {
        System.out.println("Nachricht empfangen: " + payload);
//        quiz.getInvited().add(userService.toDTO(userService.findUserByName(payload)));

        return "Nachricht empfangen: " + payload;
    }

//    @MessageMapping("/next")
//    @SendTo("/topic/question")
//    public DeckService.McCardDTO next(DeckService.McCardDTO card, Principal principal, @Header("deckId") String deckId) throws JsonProcessingException {
//        System.out.println("next " + card);
////        DeckService.McCardDTO card = objectMapper.readValue(payload, DeckService.McCardDTO.class);
//        DeckService.McCardDTO next = quizService.next(userService.findUserByEmail(principal.getName()), card, deckId);
////        messagingTemplate.convertAndSendToUser(principal.getName(), "/secured/user/queue", objectMapper.writeValueAsString(next));
////        messagingTemplate.convertAndSend( "/topic/next", next);
//        Quiz quiz = quizService.updatePosition(principal.getName(), card);
//        quiz.getPlayer().forEach(p -> updatePositions(quiz.getPositions(), quiz.getOwner().getId()));
//        return next;
//    }

    @MessageMapping("/quiz/positions")
    public void updatePositions(List<Integer> positions, String quiz) {
        try {
            messagingTemplate.convertAndSendToUser(quiz, "/quiz/positions", objectMapper.writeValueAsString(positions));
//            return objectMapper.writeValueAsString(positions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

//    public void sendMessageToDestination(String destination, String message) {
//        messagingTemplate.convertAndSend(destination, message);
//    }
}
