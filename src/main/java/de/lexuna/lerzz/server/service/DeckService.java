package de.lexuna.lerzz.server.service;

import de.lexuna.lerzz.model.Card;
import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.McCard;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeckService {

    @Autowired
    private DeckRepository repo;

    @Autowired
    private UserService userService;

    public List<Deck> findAll() {
        return repo.findAll();
    }

    public void addDeck(DeckDTO deck, User user) {
        repo.insert(new Deck(user.getUsername(), deck.getName(), deck.getDescription(), Instant.now()));
    }

    public void deleteDeck(String deckId) {
        repo.deleteById(deckId);
    }

    public Deck getDeckDyId(String stackId) {
        return repo.findById(stackId).get();
    }

    public void addCard(User user, McCardDTO cardDTO) {
        Deck deck = getDeckDyId(cardDTO.getDeckId());
        int cardId = deck.getCards().size();
        List<String> answers = new ArrayList<>();
        answers.add(cardDTO.getAnswer1());
        answers.add(cardDTO.getAnswer2());
        answers.add(cardDTO.getAnswer3());
        answers.add(cardDTO.getAnswer4());
        deck.getCards().add(new McCard(cardId, deck.getId(), cardDTO.getQuestion(), user.getId(), answers, answers.get(cardDTO.getSolution())));
        repo.save(deck);
    }

    public List<DeckDTO> asDTOs(List<Deck> decks) {
        return decks.stream().map(d-> asDTO(d, false)).collect(Collectors.toList());
    }
    public List<McCardDTO> cardsAsDTOs(List<Card> cards) {
        return cards.stream().map(this::asDTO).collect(Collectors.toList());
    }

    public McCardDTO asDTO(Card card) {
        McCard mcCard= (McCard) card;
        return new McCardDTO(mcCard.getId(), card.getDeckId(), mcCard.getQuestion(), mcCard.getAnswers().get(0),
                mcCard.getAnswers().get(1), mcCard.getAnswers().get(2), mcCard.getAnswers().get(3), -1);
    }

    public DeckDTO asDTO(Deck deck, boolean withCards) {
//        String userName= "";
//        if(deck.getUserId()!=null && !deck.getUserId().isBlank()) {
//            userName = userService.findUserById(deck.getUserId()).getUsername();
//        }
       List<McCardDTO> cards = withCards? cardsAsDTOs(deck.getCards()) :null;
       return new DeckDTO(deck.getId(),deck.getName(), deck.getDescription(), deck.getUserId(), deck.getCreationTime().toString(), cards);
    }

    public static DeckDTO getEmptyDTO() {
        return new DeckDTO();
    }

    public McCardDTO getEmptyCardDTO() {
        return new McCardDTO();
    }

    public void update(DeckDTO deckDto) {
        Deck deck = repo.findById(deckDto.getId()).get();
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        repo.save(deck);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeckDTO {
        private String id;
        private String name;
        private String description;
        private String author;
        private String creationDate;

        private List<McCardDTO> cards;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class McCardDTO {
        private int id;

        private String deckId;
        private String question;
        private String answer1;
        private String answer2;
        private String answer3;
        private String answer4;
        private int solution;
    }
}
