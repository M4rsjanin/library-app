package com.library.dto;

import java.util.List;

public record BookDTO(
    Long id,
    String title,
    String author,
    List<String> currentBorrowersNames
){}
