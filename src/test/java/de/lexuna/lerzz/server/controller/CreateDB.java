package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.CardDeck;
import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.CardDeckRepository;
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
    private CardDeckRepository deckRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void createDB() {
        userRepo.insert(new User("admin@lerzz.de","admin", encoder.encode("admin")));

        deckRepo.insert(new CardDeck("admin", "TestDeck", "Ein Stapel zum testen", Instant.now()));
        deckRepo.insert(new CardDeck("admin", "TestDeck2", "Ein weiterer Stapel zum testen", Instant.now()));
    }
}
