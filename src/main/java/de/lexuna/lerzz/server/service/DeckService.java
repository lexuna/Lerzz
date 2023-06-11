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

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    public Deck getDeckById(String stackId) {
        return repo.findById(stackId).get();
    }

    public void addCard(String deckId, User user, McCardDTO cardDto) {
        Deck deck = getDeckById(deckId);
        int cardId = deck.getCards().size();
        deck.getCards().add(new McCard(cardId, deck.getId(), cardDto.getQuestion(), user.getId(), cardDto.getAnswers(), cardDto.getAnswers().get(cardDto.getSolution())));
        repo.save(deck);
    }

    public List<DeckDTO> asDTOs(List<Deck> decks) {
        return decks.stream().map(d -> asDTO(d, false)).collect(Collectors.toList());
    }

    public List<McCardDTO> cardsAsDTOs(List<Card> cards) {
        return cards.stream().map(this::asDTO).collect(Collectors.toList());
    }

    public McCardDTO asDTO(Card card) {
        McCard mcCard = (McCard) card;
        return new McCardDTO(mcCard.getId(), "", mcCard.getQuestion(), mcCard.getAnswers(), mcCard.getAnswers().indexOf(mcCard.getRightAnswer()));
    }

    public McCardDTO asDTO(Card card, String quizOwner) {
        McCard mcCard = (McCard) card;
        return new McCardDTO(mcCard.getId(), quizOwner, mcCard.getQuestion(), mcCard.getAnswers(), mcCard.getAnswers().indexOf(mcCard.getRightAnswer()));
    }

    public DeckDTO asDTO(Deck deck, boolean withCards) {
//        String userName= "";
//        if(deck.getUserId()!=null && !deck.getUserId().isBlank()) {
//            userName = userService.findUserById(deck.getUserId()).getUsername();
//        }
        List<McCardDTO> cards = withCards ? cardsAsDTOs(deck.getCards()) : null;
        return new DeckDTO(deck.getId(), deck.getName(), deck.getDescription(), deck.getUserId(), deck.getCreationTime().toString(), cards);
    }

    public static DeckDTO getEmptyDTO() {
        return new DeckDTO();
    }

    public McCardDTO getEmptyCardDTO() {
        McCardDTO mcCardDTO = new McCardDTO();
        mcCardDTO.setId(-1);
        mcCardDTO.answers.add("");
        mcCardDTO.answers.add("");
        mcCardDTO.answers.add("");
        mcCardDTO.answers.add("");
        return mcCardDTO;
    }

    public void update(DeckDTO deckDto) {
        Deck deck = repo.findById(deckDto.getId()).get();
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        repo.save(deck);
    }

    public void editCard(String deckId, int cardId, McCardDTO cardDto) {
        Deck deck = repo.findById(deckId).get();
        McCard card = (McCard) deck.getCards().get(cardId);
        card.setQuestion(cardDto.getQuestion());
//        List<String> answers = new ArrayList<>();
//        answers.add(cardDto.getAnswer0());
//        answers.add(cardDto.getAnswer1());
//        answers.add(cardDto.getAnswer2());
//        answers.add(cardDto.getAnswer3());
        card.setAnswers(cardDto.getAnswers());
        card.setRightAnswer(cardDto.getAnswers().get(cardDto.solution));
        repo.save(deck);
    }

    public List<Deck> getCommunityDecks(String mail) {
        User user = userService.findUserByEmail(mail);
        return findAll().stream().filter(d-> !d.getUserId().equals(user.getUsername())).collect(Collectors.toList());
    }

    public List<Deck> getUserDecks(String mail) {
        User user = userService.findUserByEmail(mail);
        return findAll().stream().filter(d-> d.getUserId().equals(user.getUsername())).collect(Collectors.toList());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeckDTO implements Serializable {
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
    public static class McCardDTO implements Serializable {
        private int id;
        private String quiz;
        private String question;

        private List<String> answers = new ArrayList<>();
//        private String answer0;
//        private String answer1;
//        private String answer2;
//        private String answer3;
        private int solution;
    }
}
