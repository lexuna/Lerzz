package de.lexuna.lerzz.model;

import de.lexuna.lerzz.server.service.DeckService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
public class Quiz {
    private static final int NR_OF_QUESTIONS = 10;

    @Id
    private String id;
    private boolean started = false;
    private User owner;
    private Deck deck;
    private List<Card> questions;
    private List<User> player = new ArrayList<>();
    private QuizMode mode;
    private final Stats stats = new Stats();
    private final List<Answer> answers = new ArrayList<>();
    private final List<Integer> positions = new ArrayList<>();

    public Card start() {
        questions = deck.getRandomQuestions(NR_OF_QUESTIONS);
        stats.start();
        started=true;
        return questions.get(1);
    }

    public boolean addAnswer(String userId, int questionNr, boolean right, String answerText) {
        McCard card = (McCard) questions.get(questionNr);
        Answer answer = new Answer(userId, questionNr, card.getAnswers().indexOf(answerText), right);
        if(answer.isRight()) {
            stats.rightAnswers += 1;
        }
        answers.add(answer);
        return card.checkAnswer(answerText); //TODO muss der check hier sein?
    }

    public Card nextQuestion(Card card) {
        return questions.get(questions.indexOf(card)+1);
    }

    public void updatePosition(User user, Card card) {
        Integer position = getPositions().get(getPlayer().indexOf(user));
        position += 1;
        positions.set(player.indexOf(user), position);
    }

    public void finish(String userId) {
        stats.stop(userId);
    }

    public int getPositionOf(User user) {
        return positions.get(player.indexOf(user));
    }

    public Card getQuestion(int cardId) {
        return questions.stream().filter(c-> c.getId() == cardId).findFirst().get();
    }

    public boolean isLastCard(DeckService.McCardDTO card) {
        return questions.get(questions.size()-1).getId() == card.getId();
    }

    public String getLink() {
        return "deck/"+deck.getId()+"/quiz/"+id;
    }

    @Getter
    public class Stats {
        private Instant begin;
        private Map<String, Instant> end = new HashMap<>();
        private int rightAnswers;

        void start() {
            begin = Instant.now();
        }

        void stop(String userId) {
            end.put(userId, Instant.now());
        }

        void addRightAnswer() {
            rightAnswers++;
        }

        public String getTime(String userId) {
            Duration between = Duration.between(begin, end.get(userId));
            return between.toMinutes()+":"+between.toSecondsPart();
        }
    }

    @Getter
    @AllArgsConstructor
    public class Answer {
        private String userId;
        private int question;
        private int answer;
        private boolean right;
    }
}
