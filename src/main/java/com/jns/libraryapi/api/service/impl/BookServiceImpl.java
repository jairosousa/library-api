package com.jns.libraryapi.api.service.impl;

import com.jns.libraryapi.api.exception.BussinessException;
import com.jns.libraryapi.api.model.entity.Book;
import com.jns.libraryapi.api.model.repository.BookRepository;
import com.jns.libraryapi.api.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())){
            throw new BussinessException("Isbn já cadastrado");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return Optional.empty();
    }
}
