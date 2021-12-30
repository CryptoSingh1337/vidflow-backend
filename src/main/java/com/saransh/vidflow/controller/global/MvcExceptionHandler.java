package com.saransh.vidflow.controller.global;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mongodb.MongoWriteException;
import com.saransh.vidflow.exceptions.ResourceNotFoundException;
import com.saransh.vidflow.exceptions.UnsupportedFormatException;
import com.saransh.vidflow.exceptions.UploadFailedException;
import com.saransh.vidflow.model.response.ErrorResponseModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * author: CryptoSingh1337
 */
@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getFieldErrors())
            errors.put(error.getField(), error.getDefaultMessage());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(APPLICATION_JSON);
        return new ResponseEntity<>(errors, header, BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseModel(e.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<?> handleJwtVerificationException(JWTVerificationException e) {
        return new ResponseEntity<>(new ErrorResponseModel(e.getMessage()), FORBIDDEN);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(new ErrorResponseModel(e.getMessage()), UNAUTHORIZED);
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<?> handleDuplicateKeyException(MongoWriteException e) {
        String errorMessage = e.getError().getMessage();
        if (e.getError().getMessage().startsWith("E11000 duplicate key error"))
            errorMessage = "Username already exists";
        return new ResponseEntity<>(new ErrorResponseModel(errorMessage), BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseModel(e.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(UnsupportedFormatException.class)
    public ResponseEntity<?> handleUnsupportedFormatException(UnsupportedFormatException e) {
        return new ResponseEntity<>(new ErrorResponseModel(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(UploadFailedException.class)
    public ResponseEntity<?> handleUploadFailedException(UploadFailedException e) {
        return new ResponseEntity<>(new ErrorResponseModel(e.getMessage()), SERVICE_UNAVAILABLE);
    }
}
