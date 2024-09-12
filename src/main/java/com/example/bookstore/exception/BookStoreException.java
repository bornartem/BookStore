package com.example.bookstore.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class BookStoreException {
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(Errors e) {
        String field = Optional.ofNullable(e.getFieldError())
                .map(FieldError::getField)
                .orElse("<unknown>");
        return ResponseEntity.badRequest().body("Поле/параметр '" + field + "' не валидно!");
    }

    @ExceptionHandler({AuthorNotFoundException.class, BookNotFoundException.class, GenreNotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
