package com.example.bookstore.base;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.dto.BookRequestDto;
import com.example.bookstore.dto.GenreDto;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Generator {

    protected final Faker faker = new Faker();

    protected Author generateAuthor() {
        Author author = new Author();
        author.setName(faker.name().firstName());
        author.setSurname(faker.name().lastName());
        author.setBirthDate(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneOffset.UTC));
        return author;
    }

    protected AuthorDto generateAuthorDto() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(faker.name().firstName());
        authorDto.setSurname(faker.name().lastName());
        authorDto.setBirthDate(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneOffset.UTC));
        return authorDto;
    }

    protected Book generateBook() {
        Book book = new Book();

        book.setTitle(faker.book().title());
        book.setYear(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneOffset.UTC).getYear());
        book.setPages(faker.random().nextInt(100, 300));

        int genres = faker.random().nextInt(1, 3);
        int authors = faker.random().nextInt(1, 2);
        book.setAuthors(
                Stream.generate(this::generateAuthor)
                        .limit(authors)
                        .collect(Collectors.toList())
        );
        book.setGenres(
                Stream.generate(this::generateGenre)
                        .limit(genres)
                        .collect(Collectors.toList())
        );
        return book;
    }

    protected BookRequestDto generateBookRequestDto() {
        BookRequestDto bookRequestDto = new BookRequestDto();

        bookRequestDto.setTitle(faker.book().title());
        bookRequestDto.setYear(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneOffset.UTC).getYear());
        bookRequestDto.setPages(faker.random().nextInt(100, 300));
        return bookRequestDto;
    }

    protected Genre generateGenre() {
        Genre genre = new Genre();

        genre.setId(faker.random().nextLong(10));
        genre.setTitle(faker.book().genre());
        return genre;
    }

    protected GenreDto generateGenreDto() {
        GenreDto genreDto = new GenreDto();

        genreDto.setTitle(faker.book().genre());
        return genreDto;
    }
}
