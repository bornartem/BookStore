package com.example.bookstore.exception;

public class BookNotFoundException extends RuntimeException{

    private final long id;

    public BookNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Книга с id " + id + " не найден";
    }
}
