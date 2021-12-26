package com.saransh.vidflow.bootstrap;

import com.saransh.vidflow.domain.Comment;
import com.saransh.vidflow.domain.User;
import com.saransh.vidflow.domain.Video;
import com.saransh.vidflow.domain.VideoStatus;
import com.saransh.vidflow.repositories.UserRepository;
import com.saransh.vidflow.repositories.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.debug("Creating sample users...");
            userRepository.saveAll(createUsers());
        }
        log.debug("Total users: {}", userRepository.count());
        if (videoRepository.count() == 0) {
            log.debug("Saving videos...");
            videoRepository.saveAll(createVideos());
        }
        log.debug("Total videos: {}", videoRepository.count());
    }

    private List<User> createUsers() {
        List<String> channelNames = List.of(
                "CryptoSingh",
                "Dave2D",
                "Fireship",
                "ElectroBOOM",
                "Java Brains",
                "SomeOrdinaryGamer"
        );
        List<User> users = new ArrayList<>();

        int i = 1;
        for (String channelName : channelNames) {
            users.add(User.builder()
                    .username(String.format("%s_%d", "test", i))
                    .firstName("abc")
                    .lastName("abc")
                    .email("abc@xyz.com")
                    .password(encoder.encode("1234567890"))
                    .channelName(channelName)
                    .profileImage(String.format("https://avatars.dicebear.com/api/bottts/test_%d.svg", i))
                    .build());
            i += 1;
        }
        return users;
    }

    private Set<Comment> createCommentsSet() {
        Comment comment_1 = Comment.builder()
                .username("CryptoSingh")
                .body("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment_2 = Comment.builder()
                .username("Dave2D")
                .body("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment_3 = Comment.builder()
                .username("Fireship")
                .body("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment_4 = Comment.builder()
                .username("ElectroBOOM")
                .body("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment_5 = Comment.builder()
                .username("Java Brains")
                .body("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .createdAt(LocalDateTime.now())
                .build();

        return Set.of(
                comment_1,
                comment_2,
                comment_3,
                comment_4,
                comment_5);
    }

    private List<Video> createVideos() {
        Video video_1 = Video.builder()
                .title("Lorem Ipsum is simply dummy text of the printing and typesetting.")
                .channelName("CryptoSingh")
                .thumbnail("https://source.unsplash.com/1280x720/?technology")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(15000)
                .dislikes(1005)
                .views(1547856L)
                .createdAt(LocalDateTime.now().minus(Period.ofYears(3)))
                .build();

        video_1.setComments(createCommentsSet());

        Video video_2 = Video.builder()
                .title("It is a long established fact that a reader will be distracted.")
                .channelName("Dave2D")
                .thumbnail("https://source.unsplash.com/1280x720/?news")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(80456)
                .dislikes(15462)
                .views(14785623L)
                .createdAt(LocalDateTime.now().minus(Period.ofYears(3)))
                .build();

        video_2.setComments(createCommentsSet());

        Video video_3 = Video.builder()
                .title("Contrary to popular belief, Lorem Ipsum is not simply random text.")
                .channelName("Fireship")
                .thumbnail("https://source.unsplash.com/1280x720/?gaming")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(1258964)
                .dislikes(7865)
                .views(14789652325L)
                .createdAt(LocalDateTime.now().minus(Period.ofWeeks(3)))
                .build();

        video_3.setComments(createCommentsSet());

        Video video_4 = Video.builder()
                .title("Contrary to popular belief, Lorem Ipsum is not simply random text.")
                .channelName("SomeOrdinaryGamer")
                .thumbnail("https://source.unsplash.com/1280x720/?fashion")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(65432)
                .dislikes(5874)
                .views(457874564654L)
                .createdAt(LocalDateTime.now().minus(Period.ofDays(45)))
                .build();

        video_4.setComments(createCommentsSet());

        Video video_5 = Video.builder()
                .title("There are many variations of passages of Lorem Ipsum available.")
                .channelName("ElectroBOOM")
                .thumbnail("https://source.unsplash.com/1280x720/?personal")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(75698)
                .dislikes(4563)
                .views(4165479843L)
                .createdAt(LocalDateTime.now().minus(Period.ofMonths(5)))
                .build();

        video_5.setComments(createCommentsSet());

        return List.of(
                video_1,
                video_2,
                video_3,
                video_4,
                video_5);
    }
}
