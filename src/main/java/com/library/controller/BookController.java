package com.library.controller;

import com.library.dto.BookCreateDTO;
import com.library.dto.BookDTO;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        return bookService.getAllBooks(page, size);
    }

    @PostMapping  // Endpoint pomocniczy
    public ResponseEntity<String> createBook(@Valid @RequestBody BookCreateDTO bookDto) {
        bookService.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Książka '" + bookDto.title() + "' została dodana do katalogu!");
    }
}
