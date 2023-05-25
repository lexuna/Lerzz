package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * The GameRepository extends the MongoRepository interface.
 * It provides a repository for managing game objects in a MongoDB database.
 *
 */
@Repository
public interface GameRepository extends MongoRepository<Game, Integer> {
}
