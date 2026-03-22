package com.library.repository;

import com.library.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public User findById(String libraryCardId) {
        return entityManager.find(User.class, libraryCardId);
    }

    public List<String> findBorrowedBookTitles(String cardId) {
        return entityManager.createQuery(
                "SELECT b.title FROM Book b JOIN b.borrowers u WHERE u.libraryCardId = :cardId",
                String.class)
            .setParameter("cardId", cardId)
            .getResultList();
    }


    /*  public List<User> findAll zrobione tylko po to, abym mogl testowac zmiany danych roznych uzytkowników( normalnie bym to usunal gdyz jest to out of scope, ale zostawiam
    abyscie mogli jak np w Postmanie sprawdzalem zmiane nazwisk poprzez metode
     */

    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class)
            .getResultList();
    }

    @Transactional
    public void saveOrUpdate(User user) {
        entityManager.merge(user);
    }

}