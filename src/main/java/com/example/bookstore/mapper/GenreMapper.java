package com.example.bookstore.mapper;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.dto.GenreDto;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {

    @Mapping(target = "id", ignore = true)
    public Genre toEntity(GenreDto genreDto);

    @Mapping(target = "id", ignore = true)
    void fillEntity(GenreDto genreDto, @MappingTarget Genre genre);

    GenreDto toDto(Genre genre);
}
