package com.library.service;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceBusinessLogicTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Nie powinno dodać książki, jeśli użytkownik już ją posiada")
    void shouldNotAddBookIfAlreadyInList() {

        Book ksiazka = new Book();
        ksiazka.setId(10L);
        ksiazka.setTitle("Hobbit");

        User uzytkownik = new User();
        uzytkownik.setLibraryCardId("WAW-001");
        uzytkownik.getBorrowedBooks().add(ksiazka); // ✅ usunięty zbędny if

        when(userRepository.findById("WAW-001")).thenReturn(uzytkownik);
        when(bookRepository.findById(10L)).thenReturn(ksiazka);

        userService.borrowBook("WAW-001", 10L);

        assertEquals(1, uzytkownik.getBorrowedBooks().size(), "Lista nie powinna przyjąć duplikatu");

        verify(userRepository, times(1)).findById("WAW-001");
        verify(bookRepository, times(1)).findById(10L);
    }
}