package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.model.QuizMode;
import de.lexuna.lerzz.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private Map<String, List<Quiz>> quizzes= new HashMap<>();

    @Autowired
    private UserService userService;
    @Autowired
    private DeckService decService;

    public Quiz getNewQuiz(String ownerMail, String deckId) {
        Quiz quiz = new Quiz();
        User user = userService.findUserByEmail(ownerMail);
        Deck deck = decService.getDeckDyId(deckId);

        quiz.setDeck(deck);
        quiz.setOwner(user);
        quiz.getPlayer().add(user);
        return quiz;
    }


    public static QuizService.QuizDTO getEmptyDTO() {
        return new QuizDTO();
    }

    public QuizService.QuizDTO toDTO(Quiz quiz) {
        return new QuizDTO(quiz.getId(),
                quiz.getDeck().getId(),
                quiz.getDeck().getName(),
                quiz.getOwner().getId(),
                quiz.getMode(),
                quiz.getPlayer().stream().map(userService::toDTO).collect(Collectors.toList()));//TODO implement
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizDTO implements Serializable {
        private int id;
        private String deckId;
        private String deckName;
        private String ownerId;
        private QuizMode mode;
        private List<UserService.UserDTO> invited = new ArrayList<>();
    }
}
