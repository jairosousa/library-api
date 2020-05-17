package com.jns.libraryapi.api.service.impl;

import com.jns.libraryapi.api.exception.BussinessException;
import com.jns.libraryapi.api.model.entity.Loan;
import com.jns.libraryapi.api.model.repository.LoanRepository;
import com.jns.libraryapi.api.service.LoanService;

import java.util.Optional;

public class LoanServiceImpl implements LoanService {

    private final LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if (repository.existsByBookAndNotReturned(loan.getBook())) {
            throw new BussinessException("Book already loaned");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Long update(Loan loan) {
        return null;
    }
}
