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
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return the user object
     */
    public User findByEmail(String email);

}
