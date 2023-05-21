package de.lexuna.lerzz.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.DeckService;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.security.Principal;

@Controller
public class WebSocketController {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DeckService deckService;
    @Autowired
    private UserService userService;

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/editDeck")
    public String editDeck(@Payload String payload) throws JsonProcessingException {
        System.out.println("Nachricht empfangen: " + payload);
        DeckService.DeckDTO deck = objectMapper.readValue(payload, DeckService.DeckDTO.class);
        deckService.update(deck);
        return "Nachricht empfangen: " + payload;
    }

    @MessageMapping("/invite")
    public String invite(@Payload String payload,  Authentication authentication) {
        System.out.println("Nachricht empfangen: " + payload);
//        quiz.getInvited().add(userService.toDTO(userService.findUserByName(payload)));

        return "Nachricht empfangen: " + payload;
    }

    public void sendMessageToUser(String username, String message) {
        messagingTemplate.convertAndSendToUser(username, "/topic/notification", message);
    }

//    public void sendMessageToDestination(String destination, String message) {
//        messagingTemplate.convertAndSend(destination, message);
//    }
}
