package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookRequestDto;
import com.example.bookstore.dto.BookResponseDto;
import com.example.bookstore.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    Book toEntity(BookRequestDto bookRequestDto);

    void fillEntity(BookRequestDto bookRequestDto, @MappingTarget Book book);
    BookResponseDto toDto(Book book);
}
