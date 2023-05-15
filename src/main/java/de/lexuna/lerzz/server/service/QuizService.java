package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.Quiz;
import de.lexuna.lerzz.model.QuizMode;
import de.lexuna.lerzz.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    public static QuizService.QuizDTO getEmptyDTO() {
        return new QuizDTO();
    }

    public QuizService.QuizDTO toDTO(Quiz quiz) {
        return new QuizDTO();//TODO implement
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizDTO {
        private String id;
        private String deckId;
        private String deckName;
        private String ownerId;
        private QuizMode mode;
        private List<UserService.UserDTO> invited = new ArrayList<>();
    }
}
