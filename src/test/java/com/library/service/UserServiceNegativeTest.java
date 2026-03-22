package com.library.service;

import com.library.entity.User;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Negative Path Tests for UserService")
class UserServiceNegativeTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Powinno rzucić wyjątek, gdy ID użytkownika nie istnieje w bazie")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById("UNKNOWN-ID")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.borrowBook("UNKNOWN-ID", 1L);
        });
    }

    @Test
    @DisplayName("Powinno rzucić wyjątek, gdy ID książki nie istnieje w bazie")
    void shouldThrowExceptionWhenBookNotFound() {
        when(userRepository.findById("WAW-001")).thenReturn(new User());
        when(bookRepository.findById(999L)).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> {
            userService.borrowBook("WAW-001", 999L);
        });
    }
}