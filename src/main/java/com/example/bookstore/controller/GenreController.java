package com.example.bookstore.controller;

import com.example.bookstore.dto.GenreDto;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("genres")
@Tag(name = "Жанры", description = "CRUD методы для работы с жанрами")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(summary = "Добавление жанра")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Жанр успешно добавлен"),
                    @ApiResponse(responseCode = "400", description = "Название жанра пустое"),
                    @ApiResponse(responseCode = "409", description = "Жанр с таким названием уже есть в БД"),
            }
    )
    @PostMapping
    public ResponseEntity<GenreDto> create(@RequestBody @Valid GenreDto genreDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(genreDto));
    }


    @Operation(summary = "Обновление жанра")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Жанр успешно добавлен"),
                    @ApiResponse(responseCode = "400", description = "Название жанра пустое"),
                    @ApiResponse(responseCode = "404", description = "Жанр с таким id не найден"),
            }
    )
    @PutMapping("/{id}")
    public GenreDto update(@PathVariable Long id,
                           @RequestBody @Valid GenreDto genreDto) {
        return genreService.update(id, genreDto);
    }

    @Operation(summary = "Получение жанра по id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Жанр успешно найден"),
                    @ApiResponse(responseCode = "404", description = "Жанр с таким id не найден"),
            }
    )
    @GetMapping("/{id}")
    public GenreDto get(@PathVariable Long id) {
        return genreService.get(id);
    }

    @Operation(summary = "Удаление жанра по id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Жанр успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Жанр с таким id не найден"),
            }
    )
    @DeleteMapping("/{id}")
    public GenreDto delete(@PathVariable Long id) {
        return genreService.delete(id);
    }

    @Operation(summary = "Получение списка жанров, отфильтрованных по опциональному параметру")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Жанры успешно найдены"),
            }
    )
    @GetMapping
    public List<GenreDto> list(@RequestParam(required = false, value = "title") String title) {
        if (title == null || title.isEmpty()) {
            title = null;
        }
        return genreService.list(title);
    }
}
