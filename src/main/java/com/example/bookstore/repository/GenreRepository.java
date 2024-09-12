package com.example.bookstore.repository;

import com.example.bookstore.dto.GenreDto;
import com.example.bookstore.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT new com.example.bookstore.dto.GenreDto(id, title) FROM Genre WHERE title like %:title%")
    List<GenreDto> findByTitle(@Param("title") String title);
}
