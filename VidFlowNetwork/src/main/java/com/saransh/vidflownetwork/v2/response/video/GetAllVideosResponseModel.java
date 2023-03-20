package com.saransh.vidflownetwork.v2.response.video;

import com.saransh.vidflownetwork.global.Response;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllVideosResponseModel<T> implements Response {

    Page<T> videos;
}
