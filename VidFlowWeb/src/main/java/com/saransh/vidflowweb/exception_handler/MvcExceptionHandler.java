package com.saransh.vidflowweb.exception_handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mongodb.MongoWriteException;
import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflowutilities.error.AppErrorCode;
import com.saransh.vidflowutilities.exceptions.ResourceNotFoundException;
import com.saransh.vidflowutilities.exceptions.UnsupportedFormatException;
import com.saransh.vidflowutilities.exceptions.UploadFailedException;
import com.saransh.vidflowutilities.response.ApiResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * author: CryptoSingh1337
 */
@Slf4j
@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_VAL_400));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotExistsException(UsernameNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_CLT_404));
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<?> handleJwtVerificationException(JWTVerificationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_AUTH_002));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_AUTH_003));
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<?> handleDuplicateKeyException(MongoWriteException e) {
        log.error(e.getMessage());
        if (e.getError().getMessage().startsWith("E11000 duplicate key error"))
            return ResponseEntity.status(BAD_REQUEST)
                    .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_ENT_001));
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_INT_500));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_CLT_404));
    }

    @ExceptionHandler(UnsupportedFormatException.class)
    public ResponseEntity<?> handleUnsupportedFormatException(UnsupportedFormatException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_CLT_400));
    }

    @ExceptionHandler(UploadFailedException.class)
    public ResponseEntity<?> handleUploadFailedException(UploadFailedException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_INT_500));
    }

    @ExceptionHandler(com.saransh.vidflowutilities.exceptions.MongoWriteException.class)
    public ResponseEntity<?> handleMongoWriteException(com.saransh.vidflowutilities.exceptions.MongoWriteException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(ApiResponseUtil.createApiErrorResponse(AppErrorCode.APP_INT_500));
    }
}
