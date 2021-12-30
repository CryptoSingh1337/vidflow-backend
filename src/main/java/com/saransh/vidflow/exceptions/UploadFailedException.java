package com.saransh.vidflow.exceptions;

/**
 * author: CryptoSingh1337
 */
public class UploadFailedException extends RuntimeException {

    public UploadFailedException() {
        super();
    }

    public UploadFailedException(String message) {
        super(message);
    }
}
