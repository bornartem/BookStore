package com.example.bookstore.mapper;

import com.example.bookstore.base.Generator;
import com.example.bookstore.dto.GenreDto;
import com.example.bookstore.entity.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles(profiles = {"h2-database", "without-liquibase"})
public class GenreMapperTest extends Generator {

    @Autowired
    private GenreMapper genreMapper;

    @Test
    void toEntityTest() {
        GenreDto genreDto = generateGenreDto();

        Genre actual = genreMapper.toEntity(genreDto);

        assertThat(actual.getId()).isEqualTo(genreDto.getId());
        assertThat(actual.getTitle()).isEqualTo(genreDto.getTitle());
    }

    @Test
    void fillEntityTest() {
        GenreDto genreDto = generateGenreDto();

        Genre actual = new Genre();

        genreMapper.fillEntity(genreDto, actual);
        assertThat(actual.getId()).isEqualTo(genreDto.getId());
        assertThat(actual.getTitle()).isEqualTo(genreDto.getTitle());
    }

    @Test
    void toDto(){
        Genre genre = generateGenre();

        GenreDto actual = genreMapper.toDto(genre);
        assertThat(actual.getId()).isEqualTo(genre.getId());
        assertThat(actual.getTitle()).isEqualTo(genre.getTitle());
    }
}
