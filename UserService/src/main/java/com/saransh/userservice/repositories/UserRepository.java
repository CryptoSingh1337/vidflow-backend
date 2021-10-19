package com.saransh.userservice.repositories;

import com.saransh.userservice.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by CryptSingh1337 on 10/12/2021
 */
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);
}
