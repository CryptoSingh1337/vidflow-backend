package com.saransh.vidflowutilities.exceptions;

/**
 * author: Saransh Kumar
 */
public class InternalServerException extends RuntimeException {

    public InternalServerException() {
    }

    public InternalServerException(String message) {
        super(message);
    }
}
