package com.saransh.vidflowweb.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saransh.vidflownetwork.v2.request.video.VideoMetadataRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StringToVideoMetadataRequestModel implements Converter<String, VideoMetadataRequestModel> {

    private final ObjectMapper objectMapper;

    @Override
    public VideoMetadataRequestModel convert(String s) {
        try {
            return objectMapper.readValue(s, VideoMetadataRequestModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
