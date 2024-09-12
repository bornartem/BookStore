package com.example.bookstore.exception;

public class GenreNotFoundException extends RuntimeException{
    private final long id;

    public GenreNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Жанр с id " + id + " не найден";
    }
}
