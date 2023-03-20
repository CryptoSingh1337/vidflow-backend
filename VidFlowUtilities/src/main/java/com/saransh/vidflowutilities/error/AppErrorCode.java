package com.saransh.vidflowutilities.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * @author saranshk04
 */
@Getter
public enum AppErrorCode {

    APP_CLT_404("Resource not exists", "400-001", BAD_REQUEST),
    APP_CLT_400_1("Invalid format", "400-002", BAD_REQUEST),
    APP_CLT_400_2("Invalid request", "400-003", BAD_REQUEST),
    APP_ENT_001("Resource already exists", "400-004", BAD_REQUEST),
    APP_VAL_400("Invalid request parameters", "400-005", BAD_REQUEST),
    APP_VAL_401("Invalid otp", "400-006", BAD_REQUEST),
    APP_AUTH_001("Missing Authorization header", "401-001", UNAUTHORIZED),
    APP_AUTH_002("Invalid Authentication token", "401-002", UNAUTHORIZED),
    APP_AUTH_003("Authentication token expired", "401-003", UNAUTHORIZED),
    APP_AUTH_004("Not authorized to access", "403-001", FORBIDDEN),
    APP_AUTH_005("Account not activated. Please check you email", "401-004", UNAUTHORIZED),
    APP_AUTH_006("Invalid verification url", "401-005", UNAUTHORIZED),
    APP_INT_500("Internal server error", "500-001", INTERNAL_SERVER_ERROR);

    private final String message;
    private final String errorCode;
    private final HttpStatus status;

    AppErrorCode(String message) {
        this(message, null, null);
    }

    AppErrorCode(String message, HttpStatus httpStatus) {
        this(message, null, httpStatus);
    }

    AppErrorCode(String message, String errorCode, HttpStatus status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
    }

    public static AppErrorCode fromCode(String errorCode) {
        for (AppErrorCode error : AppErrorCode.values())
            if (error.errorCode.equalsIgnoreCase(errorCode))
                return error;
        return null;
    }
}
