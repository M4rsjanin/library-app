package com.library.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Testy walidacji danych wejściowych dla BookController")
class BookControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Powinno zwrócić błąd 400, gdy tytuł książki jest pusty")
    void shouldReturn400WhenTitleIsBlank() throws Exception {
        String invalidBookJson = """
            {
                "title": "",
                "author": "Jan Kowalski",
                "authorFirstName": "Jan",
                "authorLastName": "Kowalski",
                "publisher": "Wydawnictwo Test",
                "publicationYear": 2024,
                "editionNumber": 1
            }
            """;

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBookJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Powinno zwrócić błąd 400, gdy tytuł jest null")
    void shouldReturn400WhenTitleIsNull() throws Exception {
        String invalidBookJson = """
            {
                "author": "Jan Kowalski",
                "authorFirstName": "Jan",
                "authorLastName": "Kowalski",
                "publisher": "Wydawnictwo Test",
                "publicationYear": 2024,
                "editionNumber": 1
            }
            """;

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBookJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Powinno zwrócić 201, gdy dane są poprawne")
    void shouldReturn201WhenDataIsValid() throws Exception {
        String validBookJson = """
            {
                "title": "Hobbit",
                "author": "J.R.R. Tolkien",
                "authorFirstName": "J.R.R.",
                "authorLastName": "Tolkien",
                "publisher": "Allen & Unwin",
                "publicationYear": 1937,
                "editionNumber": 1
            }
            """;

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validBookJson))
            .andExpect(status().isCreated());
    }
}