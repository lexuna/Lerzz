package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * The CardRepository extends the MongoRepository interface.
 * It provides a repository for managing card objects in a MongoDB database.
 *
 */
@Repository
public interface CardRepository extends MongoRepository<Card, Integer> {
}
