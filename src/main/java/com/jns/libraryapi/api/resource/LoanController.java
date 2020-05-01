package com.jns.libraryapi.api.resource;

import com.jns.libraryapi.api.dto.LoanDTO;
import com.jns.libraryapi.api.model.entity.Book;
import com.jns.libraryapi.api.model.entity.Loan;
import com.jns.libraryapi.api.service.BookService;
import com.jns.libraryapi.api.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO loanDTO) {
        Book book = bookService.getBookByIsbn(loanDTO.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book not found for passed isbn"));

        Loan entity = Loan.builder()
                .book(book)
                .custumer(loanDTO.getCustumer())
                .loaanDate(LocalDate.now())
                .build();
        entity = loanService.save(entity);

        return entity.getId();
    }
}
