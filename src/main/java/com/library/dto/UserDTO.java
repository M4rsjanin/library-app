package com.library.dto;

import java.util.List;

public record UserDTO(
    String libraryCardId,
    String firstName,
    String lastName,
    List<String> borrowedBookTitles
) {}