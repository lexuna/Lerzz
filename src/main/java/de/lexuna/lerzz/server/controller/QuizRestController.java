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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller class for a REST controller to handle the operations for the quiz
 */
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


    /**
     * Controller to receive an invitation
     *
     * @param payload   with the username to invite
     * @param principal the object representing the current user
     * @throws JsonProcessingException if an error while processing JSON occurs
     */
    @PostMapping("/invite")
    public final void invite(@RequestBody final String payload, final Principal principal)
            throws JsonProcessingException {
        User user = userService.findUserByEmail(principal.getName());
        Quiz quiz = service.getQuiz(user.getId());
        Map<String, String> map = new HashMap<>();
        map.put("quiz", quiz.getDeck().getName());
        map.put("owner", user.getUsername());
        map.put("quizId", quiz.getId());
        socketController.invite(objectMapper.writeValueAsString(map), userService.findUserByName(payload).getEmail());
    }

    /**
     * Controller to receive the invitation answer
     *
     * @param payload   with boolean if the invitation is accepted
     * @param principal the object representing the current user
     * @return a list of player for the quiz
     * @throws JsonProcessingException if an error while processing JSON occurs
     */
    @PostMapping("/invitedPlayer")
    @ResponseBody
    public final String getInvited(@RequestBody final String payload, final Principal principal)
            throws JsonProcessingException {
        boolean invited = Boolean.parseBoolean(payload);
        Quiz quiz;
        if (invited) {
            quiz = service.getQuizForInvited(userService.findUserByEmail(principal.getName()));
        } else {
            quiz = service.getQuiz(userService.findUserByEmail(principal.getName()).getId());
        }
        List<String> collect = quiz.getPlayer().stream().map(p -> p.getUsername()).collect(Collectors.toList());
        return objectMapper.writeValueAsString(collect);
    }

    /**
     * Controller to get a mode change
     *
     * @param payload   with the new game mode
     * @param principal the object representing the current user
     */
    @PostMapping("/changeMode")
    public final void changeMode(@RequestBody final String payload, final Principal principal) {
        QuizMode mode = QuizMode.valueOf(payload);
        Quiz quiz = service.getQuiz(userService.findUserByEmail(principal.getName()).getId());
        quiz.setMode(mode);
    }

    /**
     * Controller to receive the invitation answer
     *
     * @param payload   with the quiz owner
     * @param principal the object representing the current user
     * @throws JsonProcessingException if an error while procession JSON occurs
     */
    @PostMapping("/invitationAccepted")
    public final void invitationAccepted(@RequestBody final String payload, final Principal principal)
            throws JsonProcessingException {
        Quiz quiz = service.getQuiz(userService.findUserByName(payload).getId());
//        quiz.getPlayer().add(userService.findUserByEmail(principal.getName()));
        Map<String, String> map = new HashMap<>();
        map.put("player", userService.findUserByEmail(principal.getName()).getUsername());
        map.put("count", quiz.getPlayer().size() + "");
        for (User player : quiz.getPlayer()) {
            socketController.invitationAccepted(objectMapper.writeValueAsString(map), player.getEmail());
        }
    }

    /**
     * Method to start the quiz
     *
     * @param payload the payload with the quiz ID
     * @return a JSON string of the first card and the quiz positions
     * @throws JsonProcessingException if an error while procession JSON occurs
     */
    @PostMapping("/card")
    @ResponseBody
    public final String startQuiz(@RequestBody final String payload, final Principal principal)
            throws JsonProcessingException {
        String quizId = (payload.substring(payload.lastIndexOf("/") + 1)).replace("}", "")
                .replace("\"", "");
        Quiz quiz = service.getQuiz(quizId);
        DeckService.McCardDTO cardDto = deckService.asDTO(quiz.getQuestions().get(0));
        Map<String, Object> map = new HashMap<>();
        map.put("card", cardDto);
        map.put("quizId", quiz.getId());
        if (quiz.getMode() == QuizMode.COOP) {
            Map<String, Object> response = new HashMap<>();
            response.put("card", cardDto);
            response.put("lastCard", quiz.isLastCard(cardDto));
            for (User player : quiz.getPlayer()) {
                if (!quiz.getOwner().getEmail().equals(player.getEmail())) {
                    socketController.next(player.getEmail(), objectMapper.writeValueAsString(response));
                }
            }
        }
        for (User player : quiz.getPlayer()) {
            socketController.updatePositions(player.getEmail(), objectMapper.writeValueAsString(quiz.getPositions()));
        }
        return objectMapper.writeValueAsString(map);
    }

    /**
     * Controller method to receive a users choice
     *
     * @param payload   the payload with the quiz ID
     * @param principal the object representing the current user
     * @throws JsonProcessingException if an error while processing JSON occurs
     */
    @PostMapping("/chose")
    public final void chose(@RequestBody final String payload, final Principal principal)
            throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(payload, Map.class);
        String quizId = (String) map.get("quizId");
        Quiz quiz = service.getQuiz(quizId);
        if (quiz.getMode() == QuizMode.COOP) {
            for (User user : quiz.getPlayer()) {
                if (!principal.getName().equals(user.getEmail())) {
                    socketController.chose(user.getEmail(), objectMapper.writeValueAsString(map.get("radioId")));
                }
            }
        }
    }

    /**
     * Controller to go to the next card
     *
     * @param payload   the payload with the deck ID, quiz ID, card ID and the solution
     * @param principal the object representing the current user
     * @return a JSON string of the next card and the quiz positions
     * @throws JsonProcessingException if an error while processing JSON occurs
     */
    @PostMapping("/next")
    @ResponseBody
    public final String next(@RequestBody final String payload, final Principal principal)
            throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(payload, Map.class);
        String quizId = (String) map.get("quizId");
        int cardId = (int) map.get("cardId");
        int solution = (int) map.get("solution");
        Quiz quiz = service.getQuiz(quizId);
        DeckService.McCardDTO card = service.next(userService.findUserByEmail(principal.getName()),
                cardId, solution, quiz);

        Map<String, Object> response = new HashMap<>();
        response.put("card", card);
        response.put("lastCard", quiz.isLastCard(card));
        for (User player : quiz.getPlayer()) {
            socketController.updatePositions(player.getEmail(), objectMapper.writeValueAsString(quiz.getPositions()));
        }
        if (quiz.getMode() == QuizMode.COOP) {
            for (User user : quiz.getPlayer()) {
                if (!principal.getName().equals(user.getEmail())) {
                    socketController.next(user.getEmail(), objectMapper.writeValueAsString(response));
                }
            }
        }
        return objectMapper.writeValueAsString(response);
    }

    /**
     * Method to end a quiz
     *
     * @param payload   the payload with the quiz ID, card ID and the solution
     * @param principal the object representing the current user
     * @throws JsonProcessingException if an error while processing JSON occurs
     */
    @PostMapping("/end")
    @ResponseBody
    public final void end(@RequestBody final String payload, final Principal principal) throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(payload, Map.class);
        String quizId = (String) map.get("quizId");
        int cardId = (int) map.get("cardId");
        int solution = (int) map.get("solution");
        Quiz quiz = service.getQuiz(quizId);
        service.end(userService.findUserByEmail(principal.getName()), cardId, solution, quiz);
    }
}
