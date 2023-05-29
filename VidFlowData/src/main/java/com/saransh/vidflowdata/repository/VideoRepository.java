package com.saransh.vidflowdata.repository;

import com.saransh.vidflowdata.entity.Category;
import com.saransh.vidflowdata.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
public interface VideoRepository extends MongoRepository<Video, String> {

    @Query(value = "{ videoStatus: 'PUBLIC' }", fields = """
            {
                id:  1,
                title:  1,
                userId: 1,
                channelName: 1,
                views: 1,
                createdAt: 1,
                thumbnail: 1
            }
            """)
    Page<Video> findAllPublicVideos(Pageable pageable);

    @Query(value = """
            {
                videoStatus: 'PUBLIC',
                title: { $regex: ?0, $options: 'i' }
            }
            """, fields = """
            {
                id: 1,
                title: 1,
                userId: 1,
                channelName: 1,
                description: 1,
                views: 1,
                createdAt: 1,
                thumbnail: 1
            }
            """)
    Page<Video> findAllByTitleRegex(String searchTitle, Pageable pageable);

    Page<Video> findAllByUsername(String username, Pageable pageable);

    Page<Video> findAllByUserId(Pageable pageable, String userId);

    @Query(value = "{ id: { $in: ?0 } }", fields = """
            {
                id:  1,
                title:  1,
                userId: 1,
                channelName: 1,
                views: 1,
                createdAt: 1,
                thumbnail: 1
            }
            """)
    Page<Video> findAllById(List<String> ids, Pageable pageable);

    @Query(value = "{ videoStatus: 'PUBLIC', userId: ?0 }", fields = """
            {
                id:  1,
                title:  1,
                userId: 1,
                channelName: 1,
                views: 1,
                createdAt: 1,
                thumbnail: 1
            }
            """)
    Page<Video> findAllByUserId(String userId, Pageable pageable);

    @Query(value = "{ videoStatus: 'PUBLIC', category: ?0, tags: { $in: ?1 } }", fields = """
            {
                id:  1,
                title:  1,
                userId: 1,
                channelName: 1,
                views: 1,
                createdAt: 1,
                thumbnail: 1
            }
            """)
    Page<Video> findAllByCategoryAndTags(Category category, List<String> tags, Pageable pageable);

    @Query(fields = "{ id: 1 }")
    List<Video> findAllByUsername(String username);

    void deleteAllByUsername(String username);
}
