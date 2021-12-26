package com.saransh.vidflow.repositories;

import com.saransh.vidflow.domain.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * author: CryptoSingh1337
 */
public interface VideoRepository extends MongoRepository<Video, String> {
}
