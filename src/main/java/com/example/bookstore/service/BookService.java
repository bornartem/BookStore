package com.example.bookstore.service;

import com.example.bookstore.dto.BookRequestDto;
import com.example.bookstore.dto.BookResponseDto;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.AuthorNotFoundException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.exception.GenreNotFoundException;
import com.example.bookstore.filter.BookFilter;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.GenreRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        Book book = bookMapper.toEntity(bookRequestDto);

        fillAuthorsAndGenres(book, bookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    public BookResponseDto update(Long id, BookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookMapper.fillEntity(bookRequestDto, book);
        fillAuthorsAndGenres(book, bookRequestDto);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    public BookResponseDto get(Long id) {
        return bookMapper.toDto(
                bookRepository.findById(id)
                        .orElseThrow(() -> new BookNotFoundException(id))
        );
    }

    @Transactional
    public BookResponseDto delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toDto(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDto> list(BookFilter bookFilter) {
        Specification<Book> specification = Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (bookFilter.getTitle() != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + bookFilter.getTitle() + "%"));
            }
            if (bookFilter.getYear() != null) {
                predicates.add(criteriaBuilder.equal(root.get("year"), bookFilter.getYear()));
            }
            if (bookFilter.getPagesFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pages"), bookFilter.getPagesFrom()));
            }
            if (bookFilter.getPagesTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pages"), bookFilter.getPagesTo()));
            }
            if (bookFilter.getAuthor() != null) {
                predicates.add(criteriaBuilder.in(root.get("authors").get("name")).in(bookFilter.getAuthor()));
            }
            if (bookFilter.getGenre() != null) {
                predicates.add(criteriaBuilder.in(root.get("genres").get("title")).in(bookFilter.getGenre()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });
        return bookRepository.findAll(specification).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    private void fillAuthorsAndGenres(Book book, BookRequestDto bookRequestDto) {
        book.setAuthors(
                bookRequestDto.getAuthorsIds().stream()
                        .map(authorId -> authorRepository.findById(authorId)
                                .orElseThrow(() -> new AuthorNotFoundException(authorId))
                        )
                        .collect(Collectors.toList())
        );
        book.setGenres(
                bookRequestDto.getGenresIds().stream()
                        .map(genreId -> genreRepository.findById(genreId)
                                .orElseThrow(() -> new GenreNotFoundException(genreId))
                        )
                        .collect(Collectors.toList())
        );
    }
}
