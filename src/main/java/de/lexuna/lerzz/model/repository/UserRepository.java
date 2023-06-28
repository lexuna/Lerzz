package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * The UserRepository extends the MongoRepository interface.
 * It provides a repository for managing user objects in a MongoDB database.
 *
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * find user by email
     * @param email of the user
     * @return user
     */
    User findByEmail(String email);

    /**
     * finds user by name
     * @param username of the user
     * @return user
     */
    User findByUsername(String username);
}
