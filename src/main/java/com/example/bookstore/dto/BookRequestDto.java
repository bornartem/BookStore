package com.example.bookstore.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class BookRequestDto {

    @NotBlank
    @Size(max = 32)
    private String title;

    @Positive
    @Max(2024)
    private Integer year;

    @Positive
    @NotNull
    private Integer pages;


    private List<Long> genresIds;

    private List<Long> authorsIds;
}
