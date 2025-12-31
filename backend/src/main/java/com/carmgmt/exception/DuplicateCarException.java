package com.carmgmt.exception;

/**
 * Exception thrown when attempting to create a duplicate car
 */
public class DuplicateCarException extends RuntimeException {
    public DuplicateCarException(String message) {
        super(message);
    }
}

