package com.saransh.vidflowdata.repository;

import com.saransh.vidflowdata.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * author: CryptoSingh1337
 */
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
}