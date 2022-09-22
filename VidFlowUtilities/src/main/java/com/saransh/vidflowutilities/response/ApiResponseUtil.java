package com.saransh.vidflowutilities.response;

import com.saransh.vidflownetwork.global.ApiResponse;
import com.saransh.vidflownetwork.global.ErrorResponse;
import com.saransh.vidflownetwork.global.Response;
import com.saransh.vidflowutilities.error.AppErrorCode;

import java.util.List;

import static com.saransh.vidflownetwork.global.ResponseStatus.ERROR;
import static com.saransh.vidflownetwork.global.ResponseStatus.SUCCESS;

/**
 * @author saranshk04
 */
public class ApiResponseUtil {

    private ApiResponseUtil() {
        throw new IllegalArgumentException("Should not be initialized");
    }

    public static <T extends Response> ApiResponse<T> createApiSuccessResponse(T data) {
        return ApiResponse.<T>builder()
                .responseStatus(SUCCESS)
                .data(data)
                .error(null)
                .build();
    }

    public static <T extends Response> ApiResponse<T> createApiErrorResponse(AppErrorCode error) {
        return ApiResponse.<T>builder()
                .responseStatus(ERROR)
                .data(null)
                .error(ErrorResponse.builder()
                        .code(error.getErrorCode())
                        .message(List.of(error.getMessage()))
                        .build())
                .build();
    }
}
