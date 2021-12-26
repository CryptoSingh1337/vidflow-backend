package com.saransh.vidflow.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    private String id;
    private String userId;
    private String username;
    private String body;
    private LocalDateTime createdAt;
}
