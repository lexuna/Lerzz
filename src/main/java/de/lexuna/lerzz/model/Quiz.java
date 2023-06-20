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
    private Instant begin;
    private final Map<String, Stats> stats = new HashMap<>();
    private final List<Answer> answers = new ArrayList<>();
    private final List<Integer> positions = new ArrayList<>();

    public Card start() {
        questions = deck.getRandomQuestions(NR_OF_QUESTIONS);
        begin = Instant.now();
        started = true;
        player.forEach(p-> stats.put(p.getEmail(), new Stats()));
        return questions.get(1);
    }


    public boolean addAnswer(String userId, int questionNr, boolean right, String answerText) {
        McCard card = (McCard) questions.get(questionNr);
        Answer answer = new Answer(userId, questionNr, card.getAnswers().indexOf(answerText), right);
        if (answer.isRight()) {
            stats.get(userId).rightAnswers += 1;
        }
        answers.add(answer);
        return card.checkAnswer(answerText); //TODO muss der check hier sein?
    }

    public Card nextQuestion(Card card) {
        return questions.get(questions.indexOf(card) + 1);
    }

    public void updatePosition(User user, Card card) {
        if (mode == QuizMode.VS) {
            Integer position = getPositions().get(getPlayer().indexOf(user));
            positions.set(player.indexOf(user), position+1);
        } else {
            for (int i = 0; i < positions.size(); i++) {
                positions.set(i, positions.get(i) + 1);
            }
        }
    }

    public void finish(String userId) {
        stats.get(userId).stop();
    }

    public int getPositionOf(User user) {
        return positions.get(player.indexOf(user));
    }

    public Card getQuestion(int cardId) {
        return questions.stream().filter(c -> c.getId() == cardId).findFirst().get();
    }

    public boolean isLastCard(DeckService.McCardDTO card) {
        return questions.get(questions.size() - 1).getId() == card.getId();
    }

    public String getLink() {
        return "deck/" + deck.getId() + "/quiz/" + id;
    }

    public String getTime(String userId) {
        Duration between = Duration.between(begin, stats.get(userId).end);
        return between.toMinutes() + ":" + between.toSecondsPart();
    }

    @Getter
    public class Stats {
        private int rightAnswers;
        private Instant end;

        void stop() {
            end = Instant.now();
        }

        public void addRightAnswer() {
            rightAnswers++;
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
