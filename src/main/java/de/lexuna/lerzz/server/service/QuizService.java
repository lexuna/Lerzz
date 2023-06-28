package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.*;
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

/**
 * Class to manage quizzes
 */
@Service
public class QuizService {

    private Map<String, Quiz> quizzes = new HashMap<>();
    private Map<String, Quiz> quizzesByOwner = new HashMap<>();

    @Autowired
    private UserService userService;
    @Autowired
    private DeckService deckService;

    /**
     * Method to create a new quiz with a given owner and deck
     *
     * @param ownerMail the email of the quiz owner
     * @param deckId    the ID of the deck
     * @return a new quiz
     */
    public Quiz getNewQuiz(String ownerMail, String deckId) {
        Quiz quiz = new Quiz();
        User user = userService.findUserByEmail(ownerMail);
        Deck deck = deckService.getDeckById(deckId);

        quiz.setId(user.getId());
        quiz.setDeck(deck);
        quiz.setOwner(user);
        quiz.getPlayer().add(user);
        quizzes.put(quiz.getId(), quiz);
        quizzesByOwner.put(user.getId(), quiz);
        return quiz;
    }

    /**
     * Method to get a quiz by the given ID
     *
     * @param id the ID of the quiz
     * @return the quiz with the given ID
     */
    public Quiz getQuiz(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }
        Quiz quiz = quizzes.get(id);
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz with id " + id + " not found");
        }
        return quiz;
    }

    /**
     * Method to get a new empty quiz DTO
     *
     * @return an empty quiz DTO
     */
    public static QuizService.QuizDTO getEmptyDTO() {
        return new QuizDTO();
    }

    /**
     * Method to convert a quiz into a quiz DTO
     *
     * @param quiz the quiz that should be converted
     * @return the converted quiz as DTO
     */
    public QuizService.QuizDTO toDTO(Quiz quiz) {
        return new QuizDTO(quiz.getId(),
                quiz.getDeck().getId(),
                quiz.getDeck().getName(),
                quiz.getOwner().getId(),
                quiz.getMode(),
                quiz.getPlayer().stream().map(userService::toDTO).collect(Collectors.toList()));
    }

    /**
     * Method to start a quiz with the given quiz DTO
     *
     * @return the method to start the quiz
     */
    public Card start(Quiz quiz) {
        quiz.setMode(quiz.getMode());
        quiz.setPlayer(quiz.getPlayer().stream().map(i -> userService.findUserById(i.getId())).collect(Collectors.toList()));
        quiz.getPlayer().forEach(p -> quiz.getPositions().add(1));
        return quiz.start();
    }

    /**
     * Method to go to the next question of the quiz
     *
     * @param user     the user answering the quiz
     * @param cardId   the ID ot the current card
     * @param solution the chosen answer
     * @param quiz     the quiz object
     * @return the card DTO of the next question
     */
    public DeckService.McCardDTO next(User user, int cardId, int solution, Quiz quiz) {
        McCard card = (McCard) quiz.getQuestion(cardId);
        String answer = card.getAnswers().get(solution);
        if (quiz.getMode() == QuizMode.COOP) {
//            for (User player : quiz.getPlayer()) {
                boolean right = quiz.addAnswer(quiz.getOwner().getEmail(), card, answer);
                if (right) {
                    quiz.getStats().get(quiz.getOwner().getEmail()).addRightAnswer();
                }
//            }
        } else {
            boolean right = quiz.addAnswer(user.getEmail(), card, answer);
            if (right) {
                quiz.getStats().get(user.getEmail()).addRightAnswer();
            }
        }
        updatePosition(user, quiz);
        return deckService.asDTO(quiz.nextQuestion(card), quiz.getId());
    }

    /**
     * Method to end the quiz
     *
     * @param user     the user answering the quiz
     * @param cardId   the ID of the current card
     * @param solution the chosen answer
     * @param quiz     the quiz object
     */
    public void end(User user, int cardId, int solution, Quiz quiz) {
        McCard card = (McCard) quiz.getDeck().getCards().get(cardId);
        String answer = card.getAnswers().get(solution);
        if (quiz.getMode() == QuizMode.COOP) {
//            for (User player : quiz.getPlayer()) {
                boolean right = quiz.addAnswer( quiz.getOwner().getEmail(), card, answer);
                if (right) {
                    quiz.getStats().get(quiz.getOwner().getEmail()).addRightAnswer();
                }
//            }
        } else {
            boolean right = quiz.addAnswer(user.getEmail(), card, answer);
            if (right) {
                quiz.getStats().get(user.getEmail()).addRightAnswer();
            }
        }
    }

    /**
     * Method to update the users position in the quiz
     *
     * @param user the user answering the quiz
     * @param quiz the quiz object
     */
    public void updatePosition(User user, Quiz quiz) {
        quiz.updatePosition(user);
    }

    /**
     * Method to count the questions of a quiz DTO
     *
     * @param quiz the quiz DTO with the quiz information
     * @return the number of questions in the quiz
     */
    public int getQuestionCount(QuizDTO quiz) {
        return quizzesByOwner.get(quiz.getOwnerId()).getQuestions().size();
    }

    /**
     * @param card
     * @return
     */
    public Quiz getQuiz(DeckService.McCardDTO card) {
        return quizzesByOwner.get(card.getQuiz());
    }

    public Quiz getQuizForInvited(User user) {
        return quizzes.values().stream().filter(q -> q.getPlayer().contains(user)).findFirst().get();
    }

    public Quiz getQuizByOwner(String mail) {
        return quizzes.get(userService.findUserByEmail(mail).getId());
    }

    /**
     * Class of a DTO representing a quiz
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizDTO implements Serializable {

        private String id;
        private String deckId;
        private String deckName;
        private String ownerId;
        private QuizMode mode;
        private List<UserService.UserDTO> invited = new ArrayList<>();
    }
}
