package com.example.bookstore.service;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.entity.Author;
import com.example.bookstore.exception.AuthorNotFoundException;
import com.example.bookstore.filter.AuthorFilter;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.repository.AuthorRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }


    public AuthorDto create(AuthorDto authorDto) {
        return authorMapper.toDto(
                authorRepository.save(
                        authorMapper.toEntity(authorDto)));
    }

    public AuthorDto update(Long id, AuthorDto authorDto) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorMapper.fillEntity(authorDto, author);
        return authorMapper.toDto(authorRepository.save(author));
    }


    public AuthorDto get(Long id) {
        return authorMapper.toDto(authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id)));
    }


    public AuthorDto delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.delete(author);
        return authorMapper.toDto(author);
    }

    public List<AuthorDto> list(AuthorFilter authorFilter) {
        Specification<Author> specification = Specification.where((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (authorFilter.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + authorFilter.getName() + "%"));
            }
            if (authorFilter.getSurname() != null) {
                predicates.add(criteriaBuilder.like(root.get("surname"), "%" + authorFilter.getSurname() + "%"));
            }
            if (authorFilter.getBirthdayFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("birthDate"), authorFilter.getBirthdayFrom()));
            }
            if (authorFilter.getBirthdayTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("birthDate"), authorFilter.getBirthdayTo()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });
        return authorRepository.findAll(specification).stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }
}
