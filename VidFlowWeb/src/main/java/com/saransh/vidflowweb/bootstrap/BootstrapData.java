package com.saransh.vidflowweb.bootstrap;

import com.saransh.vidflowdata.entity.Comment;
import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflowdata.entity.Video;
import com.saransh.vidflowdata.entity.VideoStatus;
import com.saransh.vidflowdata.repository.UserRepository;
import com.saransh.vidflowdata.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

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
    @Value("${aws.cloud-front.baseUrl}")
    private String CLOUDFRONT_BASE_URL;
    @Value("${azure.cdn.baseUrl}")
    private String AZURE_CDN_BASE_URL;
    @Value("${aws.enable}")
    private Boolean AWS_ENABLE;

    @Override
    @Transactional
    public void run(String... args) {
        List<User> users = new ArrayList<>();
        if (userRepository.count() == 0) {
            log.debug("Creating sample users...");
            userRepository.saveAll(createUsers()).forEach(users::add);
            User user_1 = users.get(0);
            User user_2 = users.get(1);
            User user_3 = users.get(2);
            User user_4 = users.get(3);
            User user_5 = users.get(4);
            User user_6 = users.get(5);

            user_1.setSubscribedTo(Set.of(user_2, user_3, user_4, user_5, user_6));
            user_1.setSubscribers(Set.of(user_2, user_3, user_4, user_5, user_6));
            user_1.changeSubscriberCount();

            user_2.setSubscribedTo(Set.of(user_1, user_3, user_4, user_5, user_6));
            user_2.setSubscribers(Set.of(user_1, user_3, user_4, user_5, user_6));
            user_2.changeSubscriberCount();

            user_3.setSubscribedTo(Set.of(user_2, user_1, user_4, user_5, user_6));
            user_3.setSubscribers(Set.of(user_2, user_1, user_4, user_5, user_6));
            user_3.changeSubscriberCount();

            user_4.setSubscribedTo(Set.of(user_2, user_3, user_1, user_5, user_6));
            user_4.setSubscribers(Set.of(user_1, user_3, user_2, user_5, user_6));
            user_4.changeSubscriberCount();

            user_5.setSubscribedTo(Set.of(user_2, user_3, user_4, user_1, user_6));
            user_5.setSubscribers(Set.of(user_2, user_3, user_4, user_1, user_6));
            user_5.changeSubscriberCount();

            user_6.setSubscribedTo(Set.of(user_2, user_3, user_4, user_5, user_1));
            user_6.setSubscribers(Set.of(user_2, user_3, user_4, user_5, user_1));
            user_6.changeSubscriberCount();
            userRepository.saveAll(users);
        }
        log.debug("Total users: {}", userRepository.count());
        if (videoRepository.count() == 0) {
            log.debug("Saving videos...");
            videoRepository.saveAll(createVideos(users));
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
                    .subscribersCount(0)
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
                .id(UUID.randomUUID().toString())
                .username("test_1")
                .channelName("CryptoSingh")
                .body("Praesent vulputate luctus convallis. Etiam ac leo.")
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        Comment comment_2 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_2")
                .channelName("Dave2D")
                .body("Proin sit amet augue sit amet massa consequat dui.")
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        Comment comment_3 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_3")
                .channelName("Fireship")
                .body("Sed at aliquam ipsum. Nullam venenatis, orci eget.")
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        Comment comment_4 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_4")
                .channelName("ElectroBOOM")
                .body("Curabitur scelerisque viverra justo, eget integer.")
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        Comment comment_5 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_5")
                .channelName("Java Brains")
                .body("Maecenas imperdiet malesuada velit sed tempor nam.")
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        Comment comment_6 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_6")
                .channelName("SomeOrdinaryGamer")
                .body("Lorem ipsum dolor sit amet, consectetur tincidunt.")
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return Set.of(
                comment_1,
                comment_2,
                comment_3,
                comment_4,
                comment_5,
                comment_6);
    }

    private List<Video> createVideos(List<User> users) {
        String videoUrl = String.format("%svidflow/sample.mp4", AWS_ENABLE ? CLOUDFRONT_BASE_URL : AZURE_CDN_BASE_URL);

        Video video_1 = Video.builder()
                .title("Lorem Ipsum is simply dummy text of the printing and typesetting.")
                .userId(users.get(0).getId())
                .username(users.get(0).getUsername())
                .channelName(users.get(0).getChannelName())
                .thumbnail("https://source.unsplash.com/1280x720/?technology")
                .videoUrl(videoUrl)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(new AtomicLong(15_000))
                .dislikes(new AtomicLong(1_005))
                .views(new AtomicLong(1_547_856L))
                .createdAt(LocalDateTime.now(ZoneOffset.UTC).minus(Period.ofYears(3)))
                .build();

        video_1.setComments(createCommentsSet());

        Video video_2 = Video.builder()
                .title("It is a long established fact that a reader will be distracted.")
                .userId(users.get(1).getId())
                .username(users.get(1).getUsername())
                .channelName(users.get(1).getChannelName())
                .thumbnail("https://source.unsplash.com/1280x720/?news")
                .videoUrl(videoUrl)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(new AtomicLong(80_456))
                .dislikes(new AtomicLong(15_462))
                .views(new AtomicLong(14_785_623L))
                .createdAt(LocalDateTime.now(ZoneOffset.UTC).minus(Period.ofYears(3)))
                .build();

        video_2.setComments(createCommentsSet());

        Video video_3 = Video.builder()
                .title("Contrary to popular belief, Lorem Ipsum is not simply random text.")
                .userId(users.get(2).getId())
                .username(users.get(2).getUsername())
                .channelName(users.get(2).getChannelName())
                .thumbnail("https://source.unsplash.com/1280x720/?gaming")
                .videoUrl(videoUrl)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(new AtomicLong(1_258_964))
                .dislikes(new AtomicLong(7_865))
                .views(new AtomicLong(14_789_652_325L))
                .createdAt(LocalDateTime.now(ZoneOffset.UTC).minus(Period.ofWeeks(3)))
                .build();

        video_3.setComments(createCommentsSet());

        Video video_4 = Video.builder()
                .title("Contrary to popular belief, Lorem Ipsum is not simply random text.")
                .userId(users.get(3).getId())
                .username(users.get(3).getUsername())
                .channelName(users.get(3).getChannelName())
                .thumbnail("https://source.unsplash.com/1280x720/?fashion")
                .videoUrl(videoUrl)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(new AtomicLong(65_432))
                .dislikes(new AtomicLong(5_874))
                .views(new AtomicLong(74_564_654L))
                .createdAt(LocalDateTime.now(ZoneOffset.UTC).minus(Period.ofDays(45)))
                .build();

        video_4.setComments(createCommentsSet());

        Video video_5 = Video.builder()
                .title("There are many variations of passages of Lorem Ipsum available.")
                .userId(users.get(4).getId())
                .username(users.get(4).getUsername())
                .channelName(users.get(4).getChannelName())
                .thumbnail("https://source.unsplash.com/1280x720/?personal")
                .videoUrl(videoUrl)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem " +
                        "Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown " +
                        "printer took a galley of type and scrambled it to make a type specimen book. It has " +
                        "survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                        "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(new AtomicLong(75_698))
                .dislikes(new AtomicLong(4_563))
                .views(new AtomicLong(16_479_843L))
                .createdAt(LocalDateTime.now(ZoneOffset.UTC).minus(Period.ofMonths(5)))
                .build();

        video_5.setComments(createCommentsSet());

        Video video_6 = Video.builder()
                .title("Vestibulum vitae elit elit. In porttitor diam vitae tortor vulputate, " +
                        "quis vehicula nisi porta ante.")
                .userId(users.get(5).getId())
                .username(users.get(5).getUsername())
                .channelName(users.get(5).getChannelName())
                .thumbnail("https://source.unsplash.com/1280x720/?business")
                .videoUrl(videoUrl)
                .description("Interdum et malesuada fames ac ante ipsum primis in faucibus. Ut egestas, urna at " +
                        "ultrices imperdiet, enim tellus porttitor mi, porta rhoncus est dui vitae augue. " +
                        "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis " +
                        "egestas. Praesent et ex tortor. Pellentesque auctor lectus tellus, id egestas nunc congue " +
                        "id. In hac habitasse platea dictumst. In ac nisi augue. Nunc sed dui ultricies, lobortis " +
                        "lectus vel, egestas velit. Cras sollicitudin tincidunt ante, at volutpat.")
                .videoStatus(VideoStatus.PUBLIC)
                .likes(new AtomicLong(56_895))
                .dislikes(new AtomicLong(785))
                .views(new AtomicLong(45_786_213L))
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        video_6.setComments(createCommentsSet());

        return List.of(
                video_1,
                video_2,
                video_3,
                video_4,
                video_5,
                video_6);
    }
}
