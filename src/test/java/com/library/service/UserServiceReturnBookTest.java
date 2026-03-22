package com.library.service;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.exception.BookNotBorrowedException;
import com.library.exception.BookNotFoundException;
import com.library.exception.UserNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Jednostkowe testy dla usuwania wypożyczeń")
class UserServiceReturnBookTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Powinno pomyślnie usunąć książkę z listy wypożyczonych")
    void shouldSuccessfullyReturnBook() {
        String cardId = "WAW-001";
        Long bookId = 100L;

        Book bookToReturn = new Book();
        bookToReturn.setId(bookId);
        bookToReturn.setTitle("Wiedźmin");

        User user = new User();
        user.setLibraryCardId(cardId);

        List<Book> borrowedBooks = new ArrayList<>();
        borrowedBooks.add(bookToReturn);
        user.setBorrowedBooks(borrowedBooks);

        when(userRepository.findById(cardId)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(bookToReturn);

        assertEquals(1, user.getBorrowedBooks().size());

        userService.returnBook(cardId, bookId);

        assertTrue(user.getBorrowedBooks().isEmpty());

        verify(userRepository, times(1)).findById(cardId);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    @DisplayName("Powinno rzucić UserNotFoundException, gdy użytkownik nie istnieje")
    void shouldThrowExceptionWhenUserNotFound() {
        String cardId = "NIEISTNIEJACY-ID";
        Long bookId = 100L;

        when(userRepository.findById(cardId)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.returnBook(cardId, bookId);
        });

        verify(bookRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Powinno rzucić BookNotFoundException, gdy książka nie istnieje")
    void shouldThrowExceptionWhenBookNotFound() {
        String cardId = "WAW-001";
        Long bookId = 999L;

        User user = new User();
        user.setLibraryCardId(cardId);

        when(userRepository.findById(cardId)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> {
            userService.returnBook(cardId, bookId);
        });
    }

    @Test
    @DisplayName("Powinno rzucić BookNotBorrowedException, gdy książka nie jest wypożyczona przez użytkownika")
    void shouldThrowExceptionWhenBookNotBorrowed() {
        String cardId = "WAW-001";
        Long bookId = 100L;

        User user = new User();
        user.setLibraryCardId(cardId);
        user.setBorrowedBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(bookId);

        when(userRepository.findById(cardId)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(book);

        assertThrows(BookNotBorrowedException.class, () -> {
            userService.returnBook(cardId, bookId);
        });
    }
}