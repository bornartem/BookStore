package com.example.bookstore.controller;

import com.example.bookstore.base.Generator;
import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.dto.BookRequestDto;
import com.example.bookstore.dto.BookResponseDto;
import com.example.bookstore.dto.GenreDto;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.mapper.GenreMapper;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.GenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles = {"h2-database", "without-liquibase"})
public class BookControllerTest extends Generator {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private GenreMapper genreMapper;

    private final Faker faker = new Faker();

    @BeforeEach
    public void beforeEach() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/generate/authors").param("count", "100"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/generate/genres").param("count", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @AfterEach
    public void afterEach() {
        genreRepository.deleteAll();
        authorRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void createTest() throws Exception {
        List<Author> authors = authorRepository.findAll(PageRequest.of(0, 10)).getContent();
        List<Genre> genres = genreRepository.findAll(PageRequest.of(0, 10)).getContent();

        int authorsCount = faker.random().nextInt(1, 3);
        int genresCount = faker.random().nextInt(1, 5);

        List<AuthorDto> authorDtos = authors.stream()
                .limit(authorsCount)
                .map(author -> authorMapper.toDto(author))
                .toList();

        List<GenreDto> genreDtos = genres.stream()
                .limit(genresCount)
                .map(genre -> genreMapper.toDto(genre))
                .toList();

        BookRequestDto bookRequestDto = generateBookRequestDto();

        bookRequestDto.setAuthorsIds(
                authorDtos.stream()
                        .map(AuthorDto::getId)
                        .collect(Collectors.toList())
        );
        bookRequestDto.setGenresIds(
                genreDtos.stream()
                        .map(GenreDto::getId)
                        .collect(Collectors.toList())
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(result -> {
                    BookResponseDto bookResponseDto = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            BookResponseDto.class
                    );
                    assertThat(bookResponseDto.getYear()).isEqualTo(bookRequestDto.getYear());
                    assertThat(bookResponseDto.getPages()).isEqualTo(bookRequestDto.getPages());
                    assertThat(bookResponseDto.getTitle()).isEqualTo(bookRequestDto.getTitle());
                    assertThat(bookResponseDto.getGenres())
                            .usingRecursiveComparison()
                            .ignoringCollectionOrder()
                            .isEqualTo(genreDtos);
                    assertThat(bookResponseDto.getAuthors())
                            .usingRecursiveComparison()
                            .ignoringCollectionOrder()
                            .isEqualTo(authorDtos);
                });
    }
}
