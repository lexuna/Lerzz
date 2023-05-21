package de.lexuna.lerzz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
public class Quiz {

    private static final int NR_OF_QUESTIONS = 10;

    @Id
    private int id;
    private User owner;
    private Deck deck;
    private List<Card> questions;
    private List<User> player = new ArrayList<>();
    private QuizMode mode;
    private final Stats stats = new Stats();
    private final List<Answer> answers = new ArrayList<>();

    public Card start() {
        questions = deck.getRandomQuestions(NR_OF_QUESTIONS);
        stats.start();
        return questions.get(1);
    }

    public boolean addAnswer(String userId, int questionNr, String answerText) {
        Answer answer = new Answer(userId, questionNr, answerText);
        answers.add(answer);
        return questions.get(questionNr).checkAnswer(answerText);
    }

    public Card nextQuestion(int nr) {
        return questions.get(nr);
    }

    public void finish(String userId) {
        stats.stop(userId);
    }

    @Getter
    public class Stats {
        private Instant begin;
        private Map<String, Instant> end;
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
    }

    @Getter
    @AllArgsConstructor
    public class Answer {
        private String userId;
        private int question;
        private String answer;
    }
}
