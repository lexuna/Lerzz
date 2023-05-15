package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Quiz, Integer> {
}
