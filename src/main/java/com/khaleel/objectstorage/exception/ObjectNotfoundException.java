package com.khaleel.objectstorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotfoundException extends RuntimeException {
    public ObjectNotfoundException(String message) {
        super(message);
    }
}
