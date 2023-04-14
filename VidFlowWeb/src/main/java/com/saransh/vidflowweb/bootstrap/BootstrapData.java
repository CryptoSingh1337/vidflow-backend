package com.saransh.vidflowweb.bootstrap;

import com.saransh.vidflowdata.entity.Category;
import com.saransh.vidflowdata.entity.Comment;
import com.saransh.vidflowdata.entity.User;
import com.saransh.vidflowdata.entity.Video;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.saransh.vidflowdata.entity.VideoStatus.*;
import static java.time.ZoneOffset.UTC;

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
            users.addAll(userRepository.saveAll(createUsers()));
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
                .createdAt(LocalDateTime.now(UTC))
                .build();

        Comment comment_2 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_2")
                .channelName("Dave2D")
                .body("Proin sit amet augue sit amet massa consequat dui.")
                .createdAt(LocalDateTime.now(UTC))
                .build();

        Comment comment_3 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_3")
                .channelName("Fireship")
                .body("Sed at aliquam ipsum. Nullam venenatis, orci eget.")
                .createdAt(LocalDateTime.now(UTC))
                .build();

        Comment comment_4 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_4")
                .channelName("ElectroBOOM")
                .body("Curabitur scelerisque viverra justo, eget integer.")
                .createdAt(LocalDateTime.now(UTC))
                .build();

        Comment comment_5 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_5")
                .channelName("Java Brains")
                .body("Maecenas imperdiet malesuada velit sed tempor nam.")
                .createdAt(LocalDateTime.now(UTC))
                .build();

        Comment comment_6 = Comment.builder()
                .id(UUID.randomUUID().toString())
                .username("test_6")
                .channelName("SomeOrdinaryGamer")
                .body("Lorem ipsum dolor sit amet, consectetur tincidunt.")
                .createdAt(LocalDateTime.now(UTC))
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
        Random random = new Random();
        int maxUnlistedVideos = 10, maxPrivateVideos = 10, maxTaggedVideos = 50, totalVideos = 100;
        String TITLE = """
                There are many variations of passages of Lorem Ipsum available, but the majority have suffered
                alteration in some form, by injected humour, or randomised words which don't look even slightly
                believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't
                anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet
                tend to repeat predefined chunks as necessary, making this the first true generator on the Internet.
                It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures,
                to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free
                from repetition, injected humour, or non-characteristic words etc.
                """;
        String DESCRIPTION = """
                Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has
                been the industry's standard dummy text ever since the 1500s, when an unknown printer took
                a galley of type and scrambled it to make a type specimen book. It has survived not only
                five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.
                It was popularised in the 1960s with the release of Letraset sheets.
                """;
        String THUMBNAIL = "https://source.unsplash.com/640x360?fashion";
        String VIDEO_URL = String.format("%svidflow/sample.mp4", AWS_ENABLE ? CLOUDFRONT_BASE_URL : AZURE_CDN_BASE_URL);
        Category[] categories = Category.values();
        Set<Comment> comments = createCommentsSet();
        List<List<Integer>> randomPairsWithConstantGaps =
                generatePairsWithConstantGaps(TITLE.length(), totalVideos, random);

        List<Video> videos = new ArrayList<>();
        int currentUnlistedVideos = 0, currentPrivateVideos = 0, currentTaggedVideos = 0;
        for (int i = 0; i < totalVideos; i++) {
            int randomUser = random.nextInt(users.size());
            int randomCategory = random.nextInt(categories.length);
            User user = users.get(randomUser);
            Video video = Video.builder()
                    .title(TITLE.substring(randomPairsWithConstantGaps.get(i).get(0),
                            randomPairsWithConstantGaps.get(i).get(1)).trim())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .channelName(user.getChannelName())
                    .thumbnail(THUMBNAIL)
                    .videoUrl(VIDEO_URL)
                    .category(categories[randomCategory])
                    .description(DESCRIPTION)
                    .likes(new AtomicLong(random.nextLong(10_000_000)))
                    .dislikes(new AtomicLong(random.nextLong(10_000_000)))
                    .views(new AtomicLong(random.nextLong(10_000_000)))
                    .createdAt(generateRandomDate(random))
                    .comments(comments)
                    .build();

            if (currentPrivateVideos < maxPrivateVideos) {
                video.setVideoStatus(PRIVATE);
                currentPrivateVideos++;
            } else if (currentUnlistedVideos < maxUnlistedVideos) {
                video.setVideoStatus(UNLISTED);
                currentUnlistedVideos++;
            } else {
                if (currentTaggedVideos < maxTaggedVideos) {
                    video.setTags(List.of("music", "video"));
                    currentTaggedVideos++;
                }
                video.setVideoStatus(PUBLIC);
            }
            videos.add(video);
        }

        return videos;
    }

    private List<List<Integer>> generatePairsWithConstantGaps(int range, int n, Random random) {
        List<Integer> GAP_THRESHOLD = List.of(50, 63, 65, 66, 100);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int gap = GAP_THRESHOLD.get(random.nextInt(GAP_THRESHOLD.size()));
            int p = random.nextInt(range);
            while ((p + gap) > range)
                p = (p + gap) % range;
            result.add(List.of(p, p + gap));
        }
        return result;
    }

    private LocalDateTime generateRandomDate(Random random) {
        int p = random.nextInt(5), q = random.nextInt(10);
        return switch (p) {
            case 0 -> LocalDateTime.now(UTC).minus(Period.ofYears(q));
            case 1 -> LocalDateTime.now(UTC).minus(Period.ofMonths(q));
            case 2 -> LocalDateTime.now(UTC).minus(Period.ofWeeks(q));
            case 3 -> LocalDateTime.now(UTC).minus(Period.ofDays(q));
            case 4 -> LocalDateTime.now(UTC);
            default -> LocalDateTime.now();
        };
    }
}
