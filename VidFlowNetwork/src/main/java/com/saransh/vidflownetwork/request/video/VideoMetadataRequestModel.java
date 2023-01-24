package com.saransh.vidflownetwork.request.video;

import com.saransh.vidflowdata.entity.VideoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    private String thumbnailUrl;

    private String videoUrl;

    private List<String> tags;

    @NotBlank
    private String channelName;

    @NotBlank
    private String username;

    @NotNull
    private VideoStatus videoStatus;
}
