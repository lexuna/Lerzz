package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.Deck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.DeckRepository;
import de.lexuna.lerzz.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;

@SpringBootTest
public class CreateDB {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private DeckRepository deckRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void createDB() {
//        userRepo.insert(new User("admin@lerzz.de","admin", encoder.encode("admin")));

        deckRepo.insert(new Deck("admin", "TestDeck", "Ein Stapel zum testen", Instant.now()));
        deckRepo.insert(new Deck("admin", "TestDeck2", "Ein weiterer Stapel zum testen", Instant.now()));
    }
}
