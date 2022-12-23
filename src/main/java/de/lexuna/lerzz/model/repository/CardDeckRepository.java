package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.CardDeck;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardDeckRepository extends MongoRepository<CardDeck, Integer> {

    List<CardDeck> findByUserId(String userId);
}
