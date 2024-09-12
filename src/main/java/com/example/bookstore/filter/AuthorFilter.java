package com.example.bookstore.filter;

import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AuthorFilter {

    private String name;

    private String surname;

    @Past
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthdayFrom;

    @Past
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthdayTo;

}
