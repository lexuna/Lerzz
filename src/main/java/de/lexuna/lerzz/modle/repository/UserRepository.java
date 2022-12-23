package de.lexuna.lerzz.modle.repository;

import de.lexuna.lerzz.modle.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {


}
