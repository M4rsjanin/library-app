package com.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor

public class User {

    @Id
    @Column(name = "library_card_id", unique = true, nullable = false)
    private String libraryCardId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "user_books",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> borrowedBooks = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDate.now();
    }
}
