package com.example.bookstore.filter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookFilter {
    private String title;

    @Positive
    @Max(2024)
    private Integer year;

    @Positive
    private Integer pagesFrom;
    @Positive
    private Integer pagesTo;
    private String author;
    private String genre;
}
