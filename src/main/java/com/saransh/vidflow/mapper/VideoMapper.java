package com.saransh.vidflow.mapper;

import com.saransh.vidflow.domain.Video;
import com.saransh.vidflow.model.response.video.VideoCardResponseModel;
import org.mapstruct.Mapper;

/**
 * author: CryptoSingh1337
 */
@Mapper
public interface VideoMapper {

    VideoCardResponseModel videoToVideoCard(Video video);
}
