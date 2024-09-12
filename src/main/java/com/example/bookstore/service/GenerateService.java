package com.example.bookstore.service;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.GenreRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GenerateService {

    private final Faker faker = new Faker();
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Autowired
    public GenerateService(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    public void generateAuthors(int count) {
        Stream.generate(this::generateAuthor)
                .limit(count)
                .forEach(authorRepository::save);
    }

    public void generateBooks(int count) {
        Stream.generate(this::generateBook)
                .limit(count)
                .forEach(bookRepository::save);
    }

    public void generateGenres(int count) {
        Stream.generate(this::generateGenre)
                .limit(count)
                .forEach(genreRepository::save);
    }

    public Author generateAuthor() {
        Author author = new Author();
        author.setName(faker.name().firstName());
        author.setSurname(faker.name().lastName());
        author.setBirthDate(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneOffset.UTC));
        return author;
    }

    public Book generateBook() {
        Book book = new Book();
        book.setTitle(faker.book().genre());
        book.setYear(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneOffset.UTC).getYear());
        book.setPages(faker.number().numberBetween(100, 500));


        int generateAuthors = faker.random().nextInt(1, 3);
        int generateGenres = faker.random().nextInt(1, 4);

        book.setAuthors(
                Stream.generate(() ->
                                randomAuthor((int) authorRepository.count())
                        )
                        .limit(generateAuthors)
                        .distinct()
                        .collect(Collectors.toList())
        );

        book.setGenres(
                Stream.generate(() ->
                                randomGenre((int) genreRepository.count())
                        )
                        .limit(generateGenres)
                        .distinct()
                        .collect(Collectors.toList())
        );
        return book;
    }

    public Genre generateGenre() {
        Genre genre = new Genre();
        genre.setTitle(faker.book().genre());
        return genre;
    }

    private Author randomAuthor(int totalAuthors) {
        return authorRepository.findAll(PageRequest.of(faker.random().nextInt(totalAuthors), 1))
                .getContent().get(0);
    }

    private Genre randomGenre(int totalGenres) {
        return genreRepository.findAll(PageRequest.of(faker.random().nextInt(totalGenres), 1))
                .getContent().get(0);
    }
}
