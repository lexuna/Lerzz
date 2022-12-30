package de.lexuna.lerzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class LerzzServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(LerzzServiceMain.class, args);
    }
}
