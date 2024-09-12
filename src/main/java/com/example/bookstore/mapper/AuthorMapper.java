package com.example.bookstore.mapper;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    Author toEntity(AuthorDto authorDto);

    @Mapping(target = "id", ignore = true)
    void fillEntity(AuthorDto authorDto, @MappingTarget Author author);

    AuthorDto toDto(Author author);
}
