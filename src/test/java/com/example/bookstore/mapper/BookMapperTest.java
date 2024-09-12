package com.example.bookstore.mapper;

import com.example.bookstore.base.Generator;
import com.example.bookstore.dto.BookRequestDto;
import com.example.bookstore.dto.BookResponseDto;
import com.example.bookstore.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles(profiles = {"h2-database", "without-liquibase"})
public class BookMapperTest extends Generator {

    @Autowired
    private BookMapper bookMapper;

    @Test
    void toEntityTest() {
        BookRequestDto bookRequestDto = generateBookRequestDto();

        Book actual = bookMapper.toEntity(bookRequestDto);

        assertThat(actual.getTitle()).isEqualTo(bookRequestDto.getTitle());
        assertThat(actual.getYear()).isEqualTo(bookRequestDto.getYear());
        assertThat(actual.getPages()).isEqualTo(bookRequestDto.getPages());
        assertThat(actual.getAuthors()).isNull();
        assertThat(actual.getGenres()).isNull();
    }

    @Test
    void fillEntityTest() {
        BookRequestDto bookRequestDto = generateBookRequestDto();

        Book actual = new Book();

        bookMapper.fillEntity(bookRequestDto, actual);
        assertThat(actual.getTitle()).isEqualTo(bookRequestDto.getTitle());
        assertThat(actual.getYear()).isEqualTo(bookRequestDto.getYear());
        assertThat(actual.getPages()).isEqualTo(bookRequestDto.getPages());
        assertThat(actual.getAuthors()).isNull();
        assertThat(actual.getGenres()).isNull();
    }

    @Test
    void toDtoTest() {
        Book book = generateBook();

        BookResponseDto actual = bookMapper.toDto(book);

        assertThat(actual.getId()).isEqualTo(book.getId());
        assertThat(actual.getTitle()).isEqualTo(book.getTitle());
        assertThat(actual.getYear()).isEqualTo(book.getYear());
        assertThat(actual.getPages()).isEqualTo(book.getPages());
        assertThat(actual.getAuthors())
                .usingRecursiveComparison()
                .isEqualTo(book.getAuthors());
        assertThat(actual.getGenres())
                .usingRecursiveComparison()
                .isEqualTo(book.getGenres());
    }
}
