package com.saransh.vidflowutilities.exceptions;

/**
 * author: CryptoSingh1337
 */
public class MongoWriteException extends RuntimeException {

    public MongoWriteException() {
        super();
    }

    public MongoWriteException(String message) {
        super(message);
    }
}
