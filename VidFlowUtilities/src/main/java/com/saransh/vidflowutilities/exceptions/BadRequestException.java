package com.saransh.vidflowutilities.exceptions;

/**
 * author: CryptoSingh1337
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
