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

    private Map<String, Quiz> quizzes= new HashMap<>();
    private Map<String, Quiz> quizzesByOwner= new HashMap<>();

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
        return quizzes.get(id);
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
                quiz.isStarted(),
                quiz.getPlayer().stream().map(userService::toDTO).collect(Collectors.toList()));
    }

    public Card start(QuizDTO quizDto) {
        Quiz quiz = quizzesByOwner.get(quizDto.getOwnerId());
        quiz.setMode(quizDto.getMode());
        quiz.setPlayer(quizDto.getInvited().stream().map(i-> userService.findUserById(i.getId())).collect(Collectors.toList()));
        quiz.getPlayer().forEach(p-> quiz.getPositions().add(1));
        quizDto.setStarted(true);
        return quiz.start();
    }

    public DeckService.McCardDTO next(User user, int cardId, int solution, Quiz quiz) {
        McCard card = (McCard) quiz.getDeck().getCards().get(cardId);
//        McCard card =  quiz.getQuestions().get(questionNr);
        String answer = card.getAnswers().get(solution);
        quiz.addAnswer(user.getEmail(), quiz.getQuestions().indexOf(card), card.checkAnswer(answer), answer);
        updatePosition(user, quiz, card);
        return deckService.asDTO(quiz.nextQuestion(card), quiz.getId());
    }

    public void end(User user, int cardId, int solution, Quiz quiz) {
        McCard card = (McCard) quiz.getDeck().getCards().get(cardId);
//        McCard card =  quiz.getQuestions().get(questionNr);
        String answer = card.getAnswers().get(solution);
        quiz.addAnswer(user.getEmail(), quiz.getQuestions().indexOf(card), card.checkAnswer(answer), answer);
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

        private boolean started;
        private List<UserService.UserDTO> invited = new ArrayList<>();
    }
}
