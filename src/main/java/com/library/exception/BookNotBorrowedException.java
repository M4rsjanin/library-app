package com.library.exception;

public class BookNotBorrowedException extends RuntimeException {
    public BookNotBorrowedException(String cardId, Long bookId) {
        super("Użytkownik o ID: " + cardId + " nie posiada wypożyczonej książki o ID: " + bookId);
    }
}