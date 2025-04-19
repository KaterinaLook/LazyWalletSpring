package com.lazywallet.lazywallet.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email is already registered.");
    }
}
