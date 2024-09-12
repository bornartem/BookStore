package com.example.bookstore.controller;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.filter.AuthorFilter;
import com.example.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("author")
@Tag(name = "Авторы", description = "CRUD методы по работе с авторами")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "Добавление автора")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Автор успешно добавлен"),
                    @ApiResponse(responseCode = "400", description = "Невалидное поле DTO"),
            }
    )
    @PostMapping
    public ResponseEntity<AuthorDto> create(@RequestBody @Valid AuthorDto authorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(authorDto));
    }


    @Operation(summary = "Обновление автора по id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Автор успешно добавлен"),
                    @ApiResponse(responseCode = "400", description = "Невалидное поле DTO"),
                    @ApiResponse(responseCode = "404", description = "Автор с таким id не найден"),
            }
    )
    @PutMapping("/{id}")
    public AuthorDto update(@PathVariable Long id,
                            @RequestBody @Valid AuthorDto authorDto) {
        return authorService.update(id, authorDto);
    }

    @Operation(summary = "Получение автора по id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Автор успешно найден"),
                    @ApiResponse(responseCode = "404", description = "Автор с таким id не найден"),
            }
    )
    @GetMapping("/{id}")
    public AuthorDto get(@PathVariable Long id) {
        return authorService.get(id);
    }

    @Operation(summary = "Удаление автора по id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Автор успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Автор с таким id не найден"),
            }
    )
    @DeleteMapping("/{id}")
    public AuthorDto delete(@PathVariable Long id) {
        return authorService.delete(id);
    }

    @Operation(summary = "Получение списка авторов, отфильтрованных по опциональному параметру")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Жанры успешно найдены"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры фильтрации"),
            }
    )
    @GetMapping
    public List<AuthorDto> list(@Valid AuthorFilter authorFilter) {
        return authorService.list(authorFilter);
    }
}
