package com.library.service;

import com.library.dto.UserDTO;
import com.library.entity.Book;
import com.library.entity.User;
import com.library.exception.BookNotBorrowedException;
import com.library.exception.BookNotFoundException;
import com.library.exception.UserNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private static final long CACHE_DURATION_MS = 5 * 60 * 1000;
    private record CacheEntry(List<String> titles, long timestamp) {}
    private final Map<String, CacheEntry> customCache = new ConcurrentHashMap<>();


    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> new UserDTO(
                user.getLibraryCardId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBorrowedBooks().stream()
                    .map(Book::getTitle)
                    .toList()
            ))
            .toList();
    }

    @Transactional
    public void borrowBook(String cardId, Long bookId) {
        User user = userRepository.findById(cardId);
        if (user == null) {
            throw new UserNotFoundException(cardId);
        }

        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }

        if (!user.getBorrowedBooks().contains(book)) {
            user.getBorrowedBooks().add(book);
            customCache.remove(cardId);
        }
    }

    @Transactional(readOnly = true)
    public List<String> getBorrowedTitlesForUser(String cardId) {
        long currentTime = System.currentTimeMillis();
        CacheEntry entry = customCache.get(cardId);

        if (entry != null && (currentTime - entry.timestamp()) < CACHE_DURATION_MS) {
            System.out.println("Pobieram tytuły z własnego cache dla: " + cardId);
            return entry.titles();
        }

        System.out.println("Pobieram tytuły z bazy danych dla: " + cardId);
        List<String> titlesFromDb = userRepository.findBorrowedBookTitles(cardId);

        customCache.put(cardId, new CacheEntry(titlesFromDb, currentTime));

        return titlesFromDb;
    }

    @Transactional
    public void updateUserName(String cardId, String newLastName) {
        User user = userRepository.findById(cardId);
        if (user == null) {
            throw new UserNotFoundException(cardId);
        }
        user.setLastName(newLastName);
    }

    @Transactional
    public void returnBook(String cardId, Long bookId) {
        User user = userRepository.findById(cardId);
        if (user == null) {
            throw new UserNotFoundException(cardId);
        }

        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }

        if (!user.getBorrowedBooks().contains(book)) {
            throw new BookNotBorrowedException(cardId, bookId);
        }

        user.getBorrowedBooks().remove(book);
        customCache.remove(cardId);
    }
}