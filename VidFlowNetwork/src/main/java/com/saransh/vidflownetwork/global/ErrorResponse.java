package com.saransh.vidflownetwork.global;

import lombok.*;

import java.util.List;

/**
 * @author saranshk04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String code;
    private List<String> message;
}
