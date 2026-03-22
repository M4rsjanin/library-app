package com.library.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long bookId) {
        super("Książka o ID: " + bookId + " nie została znaleziona.");
    }
}