package com.saransh.vidflowutilities.exceptions;

/**
 * author: CryptoSingh1337
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
