package com.saransh.vidflownetwork.v2.request.video;

import com.saransh.vidflowdata.entity.Category;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * author: CryptoSingh1337
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetRecommendedVideosRequestModel {

    @NotNull
    private Category category;
    @NotNull
    private List<String> tags;
    @NotNull
    private Integer page;
}
