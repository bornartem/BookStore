package com.example.bookstore.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class BookResponseDto {

    private Long id;

    @NotBlank
    private String title;

    @Positive
    @Max(2024)
    private Integer year;

    @Positive
    @NotNull
    private Integer pages;


    private List<GenreDto> genres;

    private List<AuthorDto> authors;
}
