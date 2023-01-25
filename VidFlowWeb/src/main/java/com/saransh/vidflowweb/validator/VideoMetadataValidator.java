package com.saransh.vidflowweb.validator;

import com.saransh.vidflownetwork.request.video.VideoMetadataRequestModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("videoMetadataValidator")
public class VideoMetadataValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return VideoMetadataRequestModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "must not be blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "must not be blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "channelName", "must not be blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "must not be blank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "videoStatus", "must not be null");

        VideoMetadataRequestModel metadata = (VideoMetadataRequestModel) target;
        if (metadata.getTitle().length() < 1 || metadata.getTitle().length() > 100) {
            errors.rejectValue("title", "size must be between 1 and 100");
        }
        if (metadata.getDescription().length() < 1 || metadata.getDescription().length() > 500) {
            errors.rejectValue("description", "size must be between 1 and 500");
        }
    }
}
