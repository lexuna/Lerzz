package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends MongoRepository<Card, Integer> {
}
