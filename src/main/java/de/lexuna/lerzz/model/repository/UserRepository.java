package de.lexuna.lerzz.model.repository;

import de.lexuna.lerzz.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    public User findByMail(String mail);

}
