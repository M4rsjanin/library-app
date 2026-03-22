package com.library.service;

import com.library.dto.BookDTO;
import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Jednostkowe testy dla BookService")
class BookServiceUnitTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("Powinno zwrócić listę zmapowanych książek")
    void shouldReturnMappedBooksList() {

        Book book = new Book();
        book.setTitle("Wiedźmin");
        book.setAuthorFirstName("Andrzej");
        book.setAuthorLastName("Sapkowski");

        List<Book> booksFromDb = List.of(book);

        when(bookRepository.findAll(anyInt(), anyInt())).thenReturn(booksFromDb);

        List<BookDTO> result = bookService.getAllBooks(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals("Wiedźmin", result.get(0).title());

        verify(bookRepository, times(1)).findAll(anyInt(), anyInt());
    }
}