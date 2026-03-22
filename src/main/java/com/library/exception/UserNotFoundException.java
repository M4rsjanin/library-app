package com.library.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String cardId) {
        super("Użytkownik o ID: " + cardId + " nie został znaleziony.");
    }
}