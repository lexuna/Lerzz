package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.Deck;
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
public interface DeckRepository extends MongoRepository<Deck, String> {

    List<Deck> findByUserId(String userId);

}
