package com.library.service;

import com.library.dto.BookCreateDTO;
import com.library.dto.BookDTO;
import com.library.entity.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks(int page, int size) {
        List<Book> books = bookRepository.findAll(page, size);

        return books.stream()
            .map(book -> new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getBorrowers().stream()
                    .map(u -> u.getFirstName() + " " + u.getLastName())
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public void addBook(BookCreateDTO dto) {
        Book book = new Book();
        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setAuthorFirstName(dto.authorFirstName());
        book.setAuthorLastName(dto.authorLastName());
        book.setPublisher(dto.publisher());
        book.setPublicationYear(dto.publicationYear());
        book.setEditionNumber(dto.editionNumber());

        bookRepository.save(book);
    }
}