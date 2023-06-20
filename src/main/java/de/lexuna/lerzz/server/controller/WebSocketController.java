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

/**
 * Class for a Websocket controller to handle Websocket messages
 */
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

    /**
     * Method to handle a WebSocket message for editing a deck
     *
     * @param payload the payload of the Websocket message
     * @return the String "Nachricht empfangen: " + the information in the given payload
     * @throws JsonProcessingException if there is an error processing the JSON payload
     */
    @MessageMapping("/editDeck")
    public String editDeck(@Payload String payload) throws JsonProcessingException {
        System.out.println("Nachricht empfangen: " + payload);
        DeckService.DeckDTO deck = objectMapper.readValue(payload, DeckService.DeckDTO.class);
        deckService.update(deck);
        return "Nachricht empfangen: " + payload;
    }

    /**
     * Method to handle a Websocket message for editing a deck
     *
     * @param payload the payload of the websocket message
     * @param user the authentication object representing the user
     * @return the String "Nachricht empfangen: " + the information in the given payload
     */
    public void invite(@Payload String payload, String user) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/invite", payload);
    }

    public void invitationAccepted(@Payload String payload, String user) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/invitationAccepted", payload);
    }

    public void start(String user, String deckId, String quizId) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/start", "/deck/"+deckId+"/quiz/"+quizId);
    }

    public void next(String user, String cardDto) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/next", cardDto);
    }

    public void chose(String email, String radioId) {
        messagingTemplate.convertAndSendToUser(email, "/queue/quiz/chose", radioId);
    }


    /**
     * Method to handle a websocket message to update the position of the players in the quiz
     *
     * @param positions the position of the players in the quiz
     *
     */
    public void updatePositions(String user, String positions) {
            messagingTemplate.convertAndSendToUser(user, "/queue/quiz/positions", positions);
    }

    public void end(String user) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/end", "");
    }

    public void addStats(String user, String payload) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/newStats", payload);
    }
}
