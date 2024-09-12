package com.example.bookstore.controller;

import com.example.bookstore.dto.BookRequestDto;
import com.example.bookstore.dto.BookResponseDto;
import com.example.bookstore.filter.BookFilter;
import com.example.bookstore.service.BookService;
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
@RequestMapping("book")
@Tag(name = "Книги", description = "CRUD методы по работе с книгами")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Добавление книги")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Книга успешно добавлена"),
                    @ApiResponse(responseCode = "400", description = "Невалидное поле DTO"),
                    @ApiResponse(responseCode = "404", description = "Жанр или автор не найден по id"),
            }
    )
    @PostMapping
    public ResponseEntity<BookResponseDto> create(@RequestBody @Valid BookRequestDto bookRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(bookRequestDto));
    }


    @Operation(summary = "Обновление книги по id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Книга успешно обновлена"),
                    @ApiResponse(responseCode = "400", description = "Невалидное поле DTO"),
                    @ApiResponse(responseCode = "404", description = "Книга, жанр или автор не найдены по id"),
            }
    )
    @PutMapping("/{id}")
    public BookResponseDto update(@PathVariable Long id,
                                  @RequestBody @Valid BookRequestDto bookRequestDto) {
        return bookService.update(id, bookRequestDto);
    }

    @Operation(summary = "Получение книги по id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Книга успешно найдена"),
                    @ApiResponse(responseCode = "404", description = "Книга с таким id не найдена"),
            }
    )
    @GetMapping("/{id}")
    public BookResponseDto get(@PathVariable Long id) {
        return bookService.get(id);
    }

    @Operation(summary = "Удаление книги по id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Книга успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Книга с таким id не найдена"),
            }
    )
    @DeleteMapping("/{id}")
    public BookResponseDto delete(@PathVariable Long id) {
        return bookService.delete(id);
    }

    @Operation(summary = "Получение списка книг, отфильтрованных по опциональному параметру")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Книги успешно найдены"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры фильтрации"),
            }
    )
    @GetMapping
    public List<BookResponseDto> list(@Valid BookFilter bookFilter) {
        return bookService.list(bookFilter);
    }
}
