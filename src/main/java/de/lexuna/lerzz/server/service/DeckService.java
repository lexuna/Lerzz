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


/**
 * Service class for managing decks and cards
 */
@Service
public class DeckService {

    @Autowired
    private DeckRepository repo;

    @Autowired
    private UserService userService;

    /**
     * Method to get all decks from the repository
     *
     * @return list of all decks in the repository
     */
    public List<Deck> findAll() {
        return repo.findAll();
    }

    /**
     * Method to add a deck to the repository
     *
     * @param deck the deck which should be added to the repository
     * @param user the user who owns the deck
     */
    public void addDeck(DeckDTO deck, User user) {
        repo.insert(new Deck(user.getUsername(), deck.getName(), deck.getDescription(), Instant.now()));
    }

    /**
     * Method to delete a deck out of the repository
     *
     * @param deckId the ID of the deck which should be removed
     */
    public void deleteDeck(String deckId) {
        repo.deleteById(deckId);
    }

    /**
     * Method to get a deck based on its ID out of the repository
     *
     * @param stackId the ID of the deck
     * @return the deck with the given ID
     */
    public Deck getDeckById(String stackId) {
        return repo.findById(stackId).get();
    }

    /**
     * Method to add a card to a deck
     *
     * @param deckId the ID of the deck
     * @param user the user who owns the deck
     * @param cardDto the card DTP witch contains the card details
     */
    public void addCard(String deckId, User user, McCardDTO cardDto) {
        Deck deck = getDeckById(deckId);
        int cardId = deck.getCards().size();
        deck.getCards().add(new McCard(cardId, deck.getId(), cardDto.getQuestion(), user.getId(), cardDto.getAnswers(), cardDto.getAnswers().get(cardDto.getSolution())));
        repo.save(deck);
    }

    /**
     * Method to turn decks into  deck DTOs (data transfer objects)
     *
     * @param decks the decks that should be turned into deck DTOs
     * @return deck DTOs of the given decks
     */
    public List<DeckDTO> asDTOs(List<Deck> decks) {
        return decks.stream().map(d -> asDTO(d, false)).collect(Collectors.toList());
    }

    /**
     * Method to turn cards into card DTOs (data transfer object)
     *
     * @param cards the cards that should be turned into card DTOs
     * @return card DTOs of the given cards
     */
    public List<McCardDTO> cardsAsDTOs(List<Card> cards) {
        return cards.stream().map(this::asDTO).collect(Collectors.toList());
    }

    /**
     * Method to turn a card into a multiple choice card DTO (data transfer object)
     *
     * @param card the card that should be turned into a multiple choice card DTO
     * @return the multiple choice card DTO
     */
    public McCardDTO asDTO(Card card) {
        McCard mcCard = (McCard) card;
        return new McCardDTO(mcCard.getId(), "", mcCard.getQuestion(), mcCard.getAnswers(), mcCard.getAnswers().indexOf(mcCard.getRightAnswer()));
    }

    /**
     * Method to turn a card into a multiple choice card DTO with an owner (data transfer object)
     *
     * @param card the card that should be turned into a multiple choice card DTO
     * @param quizOwner the owner of the quiz
     * @return the multiple choice card DTO
     */
    public McCardDTO asDTO(Card card, String quizOwner) {
        McCard mcCard = (McCard) card;
        return new McCardDTO(mcCard.getId(), quizOwner, mcCard.getQuestion(), mcCard.getAnswers(), mcCard.getAnswers().indexOf(mcCard.getRightAnswer()));
    }

    /**
     * Method to convert a deck into a deck DTO
     *
     * @param deck the deck that should be converted
     * @param withCards boolean indicating if the deck has any cards
     * @return the deck DTO of the given deck
     */
    public DeckDTO asDTO(Deck deck, boolean withCards) {
        List<McCardDTO> cards = withCards ? cardsAsDTOs(deck.getCards()) : null;
        return new DeckDTO(deck.getId(), deck.getName(), deck.getDescription(), deck.getUserId(), deck.getCreationTime().toString(), cards);
    }

    /**
     * Method to return an empty deck DTO (data transfer object)
     *
     * @return an empty deck DTO
     */
    public static DeckDTO getEmptyDTO() {
        return new DeckDTO();
    }

    /**
     * Method to return an empty multiple choice card DTO (data transfer object)
     *
     * @return an empty multiple choice card DTO
     */
    public McCardDTO getEmptyCardDTO() {
        McCardDTO mcCardDTO = new McCardDTO();
        mcCardDTO.setId(-1);
        mcCardDTO.answers.add("");
        mcCardDTO.answers.add("");
        mcCardDTO.answers.add("");
        mcCardDTO.answers.add("");
        return mcCardDTO;
    }

    /**
     * Method to update the deck DTO (data transfer object)
     *
     * @param deckDto the deck DTO that should be updated
     */
    public void update(DeckDTO deckDto) {
        Deck deck = repo.findById(deckDto.getId()).get();
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        repo.save(deck);
    }

    /**
     * Method to edit the card within a deck
     *
     * @param deckId the ID of the deck with the card to edit
     * @param cardId the ID of the card that should be edited
     * @param cardDto the card DTO of the multiple choice question
     */
    public void editCard(String deckId, int cardId, McCardDTO cardDto) {
        Deck deck = repo.findById(deckId).get();
        McCard card = (McCard) deck.getCards().get(cardId);
        card.setQuestion(cardDto.getQuestion());
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

    /**
     *  DTO class of a deck
     */
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

    /**
     * DTC class of a multiple choice card
     */
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
