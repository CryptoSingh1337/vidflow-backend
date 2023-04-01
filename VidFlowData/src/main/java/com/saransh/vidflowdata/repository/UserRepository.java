package com.saransh.vidflowdata.repository;

import com.saransh.vidflowdata.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * author: CryptoSingh1337
 */
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Query(value = "{ username: ?0 }", fields = "{ subscribedTo: 1 }")
    Optional<User> getSubscribedChannelIdByUsername(String username);

    void deleteByUsername(String username);
}
