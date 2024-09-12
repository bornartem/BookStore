package com.example.bookstore.exception;

public class AuthorNotFoundException extends RuntimeException {

    private final long id;

    public AuthorNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Автор с id " + id + " не найден";
    }
}
