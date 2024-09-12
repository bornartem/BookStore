package com.example.bookstore.mapper;

import com.example.bookstore.base.Generator;
import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.entity.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles(profiles = {"h2-database", "without-liquibase"})
public class AuthorMapperTest extends Generator {

    @Autowired
    private AuthorMapper authorMapper;

    @Test
    void toEntityTest() {
        AuthorDto authorDto = generateAuthorDto();

        Author actual = authorMapper.toEntity(authorDto);

        assertThat(actual.getName()).isEqualTo(authorDto.getName());
        assertThat(actual.getSurname()).isEqualTo(authorDto.getSurname());
        assertThat(actual.getBirthDate()).isEqualTo(authorDto.getBirthDate());
    }

    @Test
    void fillEntityTest() {
        AuthorDto authorDto = generateAuthorDto();


        Author actual = new Author();
        authorMapper.fillEntity(authorDto, actual);

        assertThat(actual.getName()).isEqualTo(authorDto.getName());
        assertThat(actual.getSurname()).isEqualTo(authorDto.getSurname());
        assertThat(actual.getBirthDate()).isEqualTo(authorDto.getBirthDate());
    }

    @Test
    void toDtoTest() {
        Author author = generateAuthor();

        AuthorDto actual = authorMapper.toDto(author);

        assertThat(actual.getName()).isEqualTo(author.getName());
        assertThat(actual.getSurname()).isEqualTo(author.getSurname());
        assertThat(actual.getBirthDate()).isEqualTo(author.getBirthDate());


    }

}
