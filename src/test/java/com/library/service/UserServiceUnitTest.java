package com.library.service;

import com.library.entity.Book;
import com.library.entity.User;
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
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Powinno dodac książkę do listy uzytkownika, gdy oba obiekty istnieja")
    void shouldAddBookToUserList() {

        User user = new User();
        user.setLibraryCardId("WAW-999");
        user.setBorrowedBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(10L);
        book.setTitle("Czysty Kod");

        when(userRepository.findById("WAW-999")).thenReturn(user);
        when(bookRepository.findById(10L)).thenReturn(book);

        userService.borrowBook("WAW-999", 10L);

        assertTrue(user.getBorrowedBooks().contains(book), "Książka powinna być na liście użytkownika");
        assertEquals(1, user.getBorrowedBooks().size());

        verify(userRepository, times(1)).findById("WAW-999");
        verify(bookRepository, times(1)).findById(10L);
    }

    @Test
    @DisplayName("Powinno rzucic wyjątek, gdy uzytkownik nie istnieje")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById("WAW-NOT-EXIST")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.borrowBook("WAW-NOT-EXIST", 1L);
        });
    }

    @Test
    @DisplayName("Cache powinien zwrócić dane bez odpytywania bazy przy drugim wywołaniu")
    void shouldReturnCachedTitlesOnSecondCall() {
        when(userRepository.findBorrowedBookTitles("WAW-999"))
            .thenReturn(List.of("Hobbit", "Wiedźmin"));

        userService.getBorrowedTitlesForUser("WAW-999");
        userService.getBorrowedTitlesForUser("WAW-999");

        verify(userRepository, times(1)).findBorrowedBookTitles("WAW-999");
    }

    @Test
    @DisplayName("Powinno pomyślnie zmienić nazwisko użytkownika")
    void shouldSuccessfullyUpdateUserLastName() {
        User user = new User();
        user.setLibraryCardId("WAW-001");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");

        when(userRepository.findById("WAW-001")).thenReturn(user);

        userService.updateUserName("WAW-001", "Nowak");

        assertEquals("Nowak", user.getLastName(), "Nazwisko powinno zostać zmienione na Nowak");
        verify(userRepository, times(1)).findById("WAW-001");
    }

    @Test
    @DisplayName("Powinno rzucić UserNotFoundException przy zmianie nazwiska gdy user nie istnieje")
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        when(userRepository.findById(anyString())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () ->
            userService.updateUserName("NIEISTNIEJACY-ID", "Nowak"));

        verify(userRepository, times(1)).findById("NIEISTNIEJACY-ID");
    }
}