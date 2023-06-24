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

/**
 * Data class for a Quiz.
 */
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

    /**
     * starts the quiz
     *
     * @return the first question of the quiz
     */
    public final Card start() {
        questions = deck.getRandomQuestions(NR_OF_QUESTIONS);
        begin = Instant.now();
        started = true;
        player.forEach(p -> stats.put(p.getEmail(), new Stats()));
        return questions.get(1);
    }


    /**
     * Adds a new Answer to the quiz.
     *
     * @param userId     of the user how gave the answer
     * @param card       of witch is answered
     * @param answerText the text of the answer
     * @return true if the answer is right
     */
    public final boolean addAnswer(final String userId,
                                   final McCard card,
                                   final String answerText) {
        Answer answer = new Answer(userId, questions.indexOf(card),
                card.getAnswers().indexOf(answerText),
                card.checkAnswer(answerText));
        if (answer.isRight()) {
            stats.get(userId).rightAnswers += 1;
        }
        answers.add(answer);
        return answer.isRight();
    }

    /**
     * geht the next card followed of the given
     *
     * @param card previous/current card
     * @return next card
     */
    public final Card nextQuestion(final Card card) {
        return questions.get(questions.indexOf(card) + 1);
    }

    /**
     * updates the user position
     *
     * @param user to update
     */
    public final void updatePosition(final User user) {
        if (mode == QuizMode.VS) {
            Integer position = getPositions().get(getPlayer().indexOf(user));
            positions.set(player.indexOf(user), position + 1);
        } else {
            for (int i = 0; i < positions.size(); i++) {
                positions.set(i, positions.get(i) + 1);
            }
        }
    }

    /**
     * finishes the quiz for one user
     *
     * @param userId of the user how finished
     */
    public final void finish(final String userId) {
        stats.get(userId).stop();
    }

    /**
     * gets the card with the given id
     * @param cardId of the card
     * @return the card
     */
    public final Card getQuestion(final int cardId) {
        return questions.stream().filter(c -> c.getId() == cardId)
                .findFirst().get();
    }

    /**
     * Checks if its the last card of the quiz
     *
     * @param card to check
     * @return true if it's the last card
     */
    public final boolean isLastCard(final DeckService.McCardDTO card) {
        return questions.get(questions.size() - 1).getId() == card.getId();
    }

    /**
     * gets the play time of a specific user
     *
     * @param userId String
     * @return time as String mm:ss
     */
    public final String getTime(final String userId) {
        Duration between = Duration.between(begin, stats.get(userId).end);
        return between.toMinutes() + ":" + between.toSecondsPart();
    }

    /**
     * Data class for the Stats of the quiz.
     */
    @Getter
    public static class Stats {
        private int rightAnswers;
        private Instant end;

        /**
         * sets the end time
         */
        final void stop() {
            end = Instant.now();
        }

        /**
         * adds one right answer to the counter
         */
        public final void addRightAnswer() {
            rightAnswers++;
        }
    }

    /**
     * data class for the given answers of the quiz
     */
    @Getter
    @AllArgsConstructor
    public static class Answer {
        private String userId;
        private int question;
        private int answer;
        private boolean right;
    }
}
