package com.saransh.vidflownetwork.global;

import lombok.*;

/**
 * @author saranshk04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T extends Response> {

    private ResponseStatus responseStatus;
    private T data;
    private ErrorResponse error;
}
