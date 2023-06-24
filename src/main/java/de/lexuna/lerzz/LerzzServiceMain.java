package de.lexuna.lerzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main class
 */
@SpringBootApplication
@EnableMongoRepositories
public abstract class LerzzServiceMain {

    /**
     * main method to run SpringBootApplication
     * @param args run args
     */
    public static void main(final String[] args) {
        SpringApplication.run(LerzzServiceMain.class, args);
    }

    /**
     * private constructor
     */
    private LerzzServiceMain() { }
}
