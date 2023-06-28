package de.lexuna.lerzz.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lexuna.lerzz.server.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Class for a Websocket controller to handle Websocket messages
 */
@Controller
public class WebSocketController {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DeckService deckService;

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
     * Method to send a Websocket message for invitation
     *
     * @param payload the payload of the websocket message
     * @param user    to send the message to
     *
     */
    public void invite(String payload, String user) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/invite", payload);
    }

    /**
     * Method to send a Websocket message for accepted invitation
     * @param payload the payload of the websocket message
     * @param user to send the message to
     */
    public void invitationAccepted(String payload, String user) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/invitationAccepted", payload);
    }

    /**
     * Method to send a Websocket message for start the quiz
     * @param user to send the message to
     * @param deckId of the quiz
     * @param quizId of the current quiz
     */
    public void start(String user, String deckId, String quizId) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/start", "/deck/" + deckId + "/quiz/" + quizId);
    }

    /**
     * Method to send a Websocket message for the next question
     * @param user to send the message to
     * @param payload message to send
     */
    public void next(String user, String payload) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/next", payload);
    }

    /**
     * Method to send a Websocket message for the taken choice
     * @param user to send the message to
     * @param payload message to send
     */
    public void chose(String user, String payload) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/chose", payload);
    }


    /**
     * Method to handle a websocket message to update the position of the players in the quiz
     *
     * @param positions the position of the players in the quiz
     */
    public void updatePositions(String user, String positions) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/positions", positions);
    }

    /**
     * Method to send a Websocket message to end question
     * @param user to send the message to
     */
    public void end(String user) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/end", "");
    }

    /**
     * Method to send a Websocket message to send the stats
     * @param user to send the message to
     * @param payload message to send
     */
    public void addStats(String user, String payload) {
        messagingTemplate.convertAndSendToUser(user, "/queue/quiz/newStats", payload);
    }
}
