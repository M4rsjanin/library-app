package com.library.controller;

import com.library.dto.UserDTO;
import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{cardId}/books")
    public List<String> getBorrowedBooks(@PathVariable String cardId) {
        return userService.getBorrowedTitlesForUser(cardId);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{cardId}/lastname")
    public ResponseEntity<String> updateLastName(
        @PathVariable String cardId,
        @RequestParam("name") String newLastName) {

        userService.updateUserName(cardId, newLastName);
        return ResponseEntity.ok("Nazwisko użytkownika " + cardId + " zostało zmienione na: " + newLastName);
    }

    @PostMapping("/{cardId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(
        @PathVariable String cardId,
        @PathVariable Long bookId) {

        userService.borrowBook(cardId, bookId);
        return ResponseEntity.ok("Użytkownik " + cardId + " wypożyczył książkę o ID: " + bookId);
    }

    @DeleteMapping("/{cardId}/borrow/{bookId}")
    public ResponseEntity<Void> returnBook(@PathVariable String cardId, @PathVariable Long bookId) {
        userService.returnBook(cardId, bookId);
        return ResponseEntity.noContent().build();
    }


}