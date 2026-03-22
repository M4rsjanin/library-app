package com.library.config;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        Book book1 = new Book();
        book1.setTitle("Wiedźmin: Ostatnie życzenie");
        book1.setAuthor("Andrzej Sapkowski");
        book1.setAuthorFirstName("Andrzej");
        book1.setAuthorLastName("Sapkowski");
        book1.setPublisher("SuperNowa");
        book1.setPublicationYear(1993);
        book1.setEditionNumber(1);

        Book book2 = new Book();
        book2.setTitle("Hobbit");
        book2.setAuthor("J.R.R. Tolkien");
        book2.setAuthorFirstName("J.R.R.");
        book2.setAuthorLastName("Tolkien");
        book2.setPublisher("Iskry");
        book2.setPublicationYear(1937);
        book2.setEditionNumber(10);

        bookRepository.save(book1);
        bookRepository.save(book2);


        User testUser = new User();
        testUser.setLibraryCardId("WAW-001");
        testUser.setFirstName("Kacper");
        testUser.setLastName("Polanski");
        testUser.getBorrowedBooks().add(book1);

        userRepository.saveOrUpdate(testUser);

        System.out.println("Testowe ksiazki są dodane");
    }
}
