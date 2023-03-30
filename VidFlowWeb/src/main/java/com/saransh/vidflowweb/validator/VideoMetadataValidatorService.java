package com.saransh.vidflowweb.validator;

import com.saransh.vidflownetwork.v2.request.video.VideoMetadataRequestModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

@Service
public class VideoMetadataValidatorService {


    private final Validator validator;

    public VideoMetadataValidatorService(@Qualifier("videoMetadataValidator") Validator validator) {
        this.validator = validator;
    }

    public void validateInput(VideoMetadataRequestModel metadata, Method method) throws MethodArgumentNotValidException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(metadata, "metadata");
        validator.validate(metadata, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(method, 2), bindingResult);
        }
    }
}
