package com.library.repository;

import com.library.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Book> findAll(int page, int size) {
        return entityManager.createQuery("SELECT b FROM Book b", Book.class)
            .setFirstResult(page * size)
            .setMaxResults(size)
            .getResultList();
    }

    public Long countAll() {
        return entityManager.createQuery("SELECT COUNT(b) FROM Book b", Long.class)
            .getSingleResult();
    }

    @Transactional
    public void save(Book book) {
        entityManager.persist(book);
    }

    public Book findById(Long id) {
        return entityManager.find(Book.class, id);
    }
}