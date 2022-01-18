package com.saransh.vidflow.repositories;

import com.saransh.vidflow.domain.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoRepository extends MongoRepository<Video, String> {
    List<Video> findAllByVideoStatusEquals(Pageable pageable, String videoStatus);
    List<Video> findAllByTitleContainingIgnoreCase(Pageable pageable, String title);
    List<Video> findAllByUserId(Pageable pageable, String userId);
    List<Video> findAllByUserId(String userId);
}
