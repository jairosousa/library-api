package com.jns.libraryapi.api.service;

import com.jns.libraryapi.api.model.entity.Book;

import java.util.Optional;

public interface BookService {

    Book save(Book any);

    Optional<Book> getById(Long id);
}
