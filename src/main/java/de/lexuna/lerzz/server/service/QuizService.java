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

@Service
public class QuizService {

    private Map<String, Quiz> quizzes = new HashMap<>();
    private Map<String, Quiz> quizzesByOwner = new HashMap<>();

    @Autowired
    private UserService userService;
    @Autowired
    private DeckService deckService;

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

    public static QuizService.QuizDTO getEmptyDTO() {
        return new QuizDTO();
    }

    public QuizService.QuizDTO toDTO(Quiz quiz) {
        return new QuizDTO(quiz.getId(),
                quiz.getDeck().getId(),
                quiz.getDeck().getName(),
                quiz.getOwner().getId(),
                quiz.getMode(),
                quiz.getPlayer().stream().map(userService::toDTO).collect(Collectors.toList()));
    }

    public Card start(Quiz quiz) {
        quiz.setMode(quiz.getMode());
        quiz.setPlayer(quiz.getPlayer().stream().map(i -> userService.findUserById(i.getId())).collect(Collectors.toList()));
        quiz.getPlayer().forEach(p -> quiz.getPositions().add(1));
        return quiz.start();
    }

    public DeckService.McCardDTO next(User user, int cardId, int solution, Quiz quiz) {
        McCard card = (McCard) quiz.getQuestion(cardId);
//        McCard card =  quiz.getQuestions().get(questionNr);
        String answer = card.getAnswers().get(solution);
        if (quiz.getMode() == QuizMode.COOP) {
            for (User player : quiz.getPlayer()) {
                boolean right = quiz.addAnswer(player.getEmail(), quiz.getQuestions().indexOf(card), card.checkAnswer(answer), answer);
                if (player.getEmail().equals(quiz.getOwner().getEmail()) && right) {
                    quiz.getStats().get(user.getEmail()).addRightAnswer();
                }
            }
        } else {
            boolean right = quiz.addAnswer(user.getEmail(), quiz.getQuestions().indexOf(card), card.checkAnswer(answer), answer);
            if(right) {
                quiz.getStats().get(user.getEmail()).addRightAnswer();
            }
        }
        updatePosition(user, quiz, card);
        return deckService.asDTO(quiz.nextQuestion(card), quiz.getId());
    }

    public void end(User user, int cardId, int solution, Quiz quiz) {
        McCard card = (McCard) quiz.getDeck().getCards().get(cardId);
//        McCard card =  quiz.getQuestions().get(questionNr);
        String answer = card.getAnswers().get(solution);
//        quiz.addAnswer(user.getEmail(), quiz.getQuestions().indexOf(card), card.checkAnswer(answer), answer);
        if (quiz.getMode() == QuizMode.COOP) {
            quiz.getPlayer().forEach(p -> quiz.addAnswer(p.getEmail(), quiz.getQuestions().indexOf(card), card.checkAnswer(answer), answer));
        } else {
            quiz.addAnswer(user.getEmail(), quiz.getQuestions().indexOf(card), card.checkAnswer(answer), answer);
        }
    }

    public void updatePosition(User user, Quiz quiz, Card card) {
        quiz.updatePosition(user, card);
    }

    public int getQuestionCount(QuizDTO quiz) {
        return quizzesByOwner.get(quiz.getOwnerId()).getQuestions().size();
    }

    public Quiz getQuiz(DeckService.McCardDTO card) {
        return quizzesByOwner.get(card.getQuiz());
    }

    public Quiz getQuizForInvited(User user) {
        return quizzes.values().stream().filter(q -> q.getPlayer().contains(user)).findFirst().get();
    }

    public Quiz getQuizByOwner(String mail) {
        return quizzes.get(userService.findUserByEmail(mail).getId());
    }

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
