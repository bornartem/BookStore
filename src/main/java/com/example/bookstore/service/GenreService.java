package com.example.bookstore.service;

import com.example.bookstore.dto.GenreDto;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.exception.GenreNotFoundException;
import com.example.bookstore.mapper.GenreMapper;
import com.example.bookstore.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    public GenreDto create(GenreDto genreDto) {
        return genreMapper.toDto(genreRepository.save(genreMapper.toEntity(genreDto)));
    }

    public GenreDto update(Long id, GenreDto genreDto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        genreMapper.fillEntity(genreDto, genre);
        return genreMapper.toDto(genre);
    }

    public GenreDto get(Long id) {
        return genreMapper.toDto(genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id)));
    }

    public GenreDto delete(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        genreRepository.delete(genre);
        return genreMapper.toDto(genre);
    }

    public List<GenreDto> list(String title) {
        return genreRepository.findByTitle(title);
    }
}
