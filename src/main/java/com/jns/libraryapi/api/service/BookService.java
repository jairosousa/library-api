package com.jns.libraryapi.api.service;

import com.jns.libraryapi.api.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Book save(Book any);

    Optional<Book> getById(Long id);

    void delete(Book book) throws IllegalAccessException;

    Book update(Book book) throws IllegalAccessException;

    Page<Book> find(Book filter, Pageable pageable);

    Optional<Book> getBookByIsbn(String isbn);
}
