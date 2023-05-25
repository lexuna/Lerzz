package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * The CardDeckRepository extends the MongoRepository interface.
 * It provides a repository for managing CardDeck in a MongoDB database.
 *
 */
@Repository
public interface CardDeckRepository extends MongoRepository<CardDeck, String> {

    /**
     *
     * @param userId the ID of the user
     * @return a list of the card decks associated with the given user ID
     *
     */
    List<CardDeck> findByUserId(String userId);

}
