package com.rev.exception;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException() {
        super();
    }

    public AlbumNotFoundException(String message) {
        super(message);
    }
}