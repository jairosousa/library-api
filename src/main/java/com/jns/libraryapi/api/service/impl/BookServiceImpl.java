package com.jns.libraryapi.api.service.impl;

import com.jns.libraryapi.api.model.entity.Book;
import com.jns.libraryapi.api.model.repository.BookRepository;
import com.jns.libraryapi.api.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
}
