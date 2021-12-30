package com.saransh.vidflow.model.request.video;

import com.saransh.vidflow.domain.VideoStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoMetadataRequestModel {

    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank
    @Size(min = 1, max = 500)
    private String description;

    @NotBlank
    private String thumbnail;

    @NotBlank
    private String videoUrl;

    private List<String> tags;

    @NotBlank
    private String channelName;

    @NotBlank
    private String username;

    @NotNull
    private VideoStatus videoStatus;
}
