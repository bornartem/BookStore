package com.example.bookstore.controller;

import com.example.bookstore.base.Generator;
import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.entity.Author;
import com.example.bookstore.filter.AuthorFilter;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.repository.AuthorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;


import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"h2-database", "without-liquibase"})
public class AuthorControllerTest extends Generator {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorMapper authorMapper;
    @MockBean
    private AuthorRepository authorRepository;

    private String baseUrl() {
        return "http://localhost:" + port + "/author";
    }

    @Test
    void createTest() throws JsonProcessingException {
        AuthorDto authorDto = generateAuthorDto();

        Author author = new Author();
        author.setId(faker.random().nextLong(10));
        author.setName(authorDto.getName());
        author.setSurname(authorDto.getSurname());
        author.setBirthDate(authorDto.getBirthDate());

        when(authorRepository.save(any())).thenReturn(author);
        ResponseEntity<AuthorDto> response = restTemplate.postForEntity(baseUrl(), authorDto, AuthorDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isEqualTo(author.getId());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .isEqualTo(author);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void createNegativeTest() {
        AuthorDto authorDto = generateAuthorDto();

        authorDto.setName(null);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl(), authorDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Поле/параметр 'name' не валидно!");

        authorDto = generateAuthorDto();

        authorDto.setSurname(null);
        response = restTemplate.postForEntity(baseUrl(), authorDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Поле/параметр 'surname' не валидно!");

        authorDto = generateAuthorDto();

        authorDto.setBirthDate(LocalDate.now().plusDays(1));
        response = restTemplate.postForEntity(baseUrl(), authorDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Поле/параметр 'birthDate' не валидно!");
    }

    @Test
    void updateTest() {
        AuthorDto oldAuthorDto = generateAuthorDto();
        AuthorDto updatedAuthorDto = generateAuthorDto();


        long id = faker.random().nextLong(10);
        Author oldAuthor = new Author();
        oldAuthor.setId(id);
        oldAuthor.setName(oldAuthorDto.getName());
        oldAuthor.setSurname(oldAuthorDto.getSurname());
        oldAuthor.setBirthDate(oldAuthorDto.getBirthDate());

        Author updatedAuthor = new Author();
        updatedAuthor.setId(id);
        updatedAuthor.setName(updatedAuthorDto.getName());
        updatedAuthor.setSurname(updatedAuthorDto.getSurname());
        updatedAuthor.setBirthDate(updatedAuthorDto.getBirthDate());

        when(authorRepository.findById(eq(id))).thenReturn(Optional.of(oldAuthor));
        when(authorRepository.save(any())).thenReturn(updatedAuthor);
        ResponseEntity<AuthorDto> response = restTemplate.exchange(
                baseUrl() + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(updatedAuthorDto),
                AuthorDto.class);

        updatedAuthorDto.setId(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .isEqualTo(updatedAuthorDto);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateNegativeTest() {
        long id = faker.random().nextLong(10);
        AuthorDto authorDto = generateAuthorDto();

        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(authorDto),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Автор с id " + id + " не найден");
    }

    @Test
    void getTest() {
        long id = faker.random().nextLong(10);

        AuthorDto authorDto = generateAuthorDto();
        authorDto.setId(id);


        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        author.setSurname(authorDto.getSurname());
        author.setBirthDate(authorDto.getBirthDate());

        when(authorRepository.findById(eq(id))).thenReturn(Optional.of(author));
        ResponseEntity<AuthorDto> response = restTemplate.getForEntity(
                baseUrl() + "/" + id,
                AuthorDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .isEqualTo(authorDto);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getNegativeTest() {
        long id = faker.random().nextLong(10);

        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl() + "/" + id,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Автор с id " + id + " не найден");
    }

    @Test
    void deleteTest() {
        long id = faker.random().nextLong(10);

        AuthorDto authorDto = generateAuthorDto();
        authorDto.setId(id);


        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        author.setSurname(authorDto.getSurname());
        author.setBirthDate(authorDto.getBirthDate());

        when(authorRepository.findById(eq(id))).thenReturn(Optional.of(author));
        ResponseEntity<AuthorDto> response = restTemplate.exchange(
                baseUrl() + "/" + id,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                AuthorDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .isEqualTo(authorDto);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void deleteNegativeTest() {
        long id = faker.random().nextLong(10);

        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/" + id,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Автор с id " + id + " не найден");
    }

    @Test
    void listTest() {
        int authors = faker.random().nextInt(10, 20);

        List<AuthorDto> authorDtoList = Stream.iterate(1L, i -> i + 1)
                .limit(authors)
                .map(id -> {
                    AuthorDto authorDto = generateAuthorDto();
                    authorDto.setId(id);
                    return authorDto;
                })
                .toList();

        String theMostCommonName = authorDtoList.get(0).getName();
        String theMostCommonSurname = authorDtoList.get(0).getSurname();
        LocalDate minBirthDate = authorDtoList.stream()
                .map(AuthorDto::getBirthDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.ofInstant(
                        faker.date().birthday().toInstant(),
                        ZoneOffset.UTC
                ));

        LocalDate maxBirthDate = authorDtoList.stream()
                .map(AuthorDto::getBirthDate)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.ofInstant(
                        faker.date().birthday().toInstant(),
                        ZoneOffset.UTC
                ));

        AuthorFilter authorFilter = new AuthorFilter();
        authorFilter.setName(theMostCommonName);
        authorFilter.setSurname(theMostCommonSurname);
        authorFilter.setBirthdayTo(minBirthDate);
        authorFilter.setBirthdayFrom(maxBirthDate);


        List<Author> filteredAuthors = authorDtoList.stream()
                .filter(authorDto ->
                        authorDto.getName().contains(authorFilter.getName())
                                && authorDto.getSurname().contains(authorFilter.getSurname())
                                && (authorDto.getBirthDate().isEqual(authorFilter.getBirthdayFrom()) || authorDto.getBirthDate().isAfter(authorFilter.getBirthdayFrom()))
                                && (authorDto.getBirthDate().isEqual(authorFilter.getBirthdayTo()) || authorDto.getBirthDate().isBefore(authorFilter.getBirthdayTo()))
                )
                .map(authorDto -> {
                    Author author = authorMapper.toEntity(authorDto);
                    author.setId(authorDto.getId());
                    return author;
                })
                .toList();

        List<AuthorDto> expected = authorDtoList.stream()
                .filter(authorDto ->
                        authorDto.getName().contains(authorFilter.getName())
                                && authorDto.getSurname().contains(authorFilter.getSurname())
                                && (authorDto.getBirthDate().isEqual(authorFilter.getBirthdayFrom()) || authorDto.getBirthDate().isAfter(authorFilter.getBirthdayFrom()))
                                && (authorDto.getBirthDate().isEqual(authorFilter.getBirthdayTo()) || authorDto.getBirthDate().isBefore(authorFilter.getBirthdayTo()))
                )
                .toList();


        when(authorRepository.findAll(any(Specification.class))).thenReturn(filteredAuthors);

        ResponseEntity<List<AuthorDto>> response = restTemplate.exchange(
                UriComponentsBuilder.fromUriString(baseUrl())
                        .queryParam("name", authorFilter.getName())
                        .queryParam("surname", authorFilter.getSurname())
                        .queryParam("birthDateFrom", authorFilter.getBirthdayFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        .queryParam("birthDateTo", authorFilter.getBirthdayTo().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        .toUriString(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
        assertThat(response.getBody()).isNotNull();
    }
}
