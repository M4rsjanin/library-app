package com.library.dto;

import jakarta.validation.constraints.NotBlank;

public record BookCreateDTO(
    @NotBlank(message = "Tytuł nie może być pusty")
    String title,

    @NotBlank(message = "Autor nie może być pusty")
    String author,

    @NotBlank(message = "Imię autora nie może być puste")
    String authorFirstName,

    @NotBlank(message = "Nazwisko autora nie może być puste")
    String authorLastName,

    String publisher,
    Integer publicationYear,
    Integer editionNumber
) {}