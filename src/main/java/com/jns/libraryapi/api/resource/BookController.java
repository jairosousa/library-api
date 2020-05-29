package com.jns.libraryapi.api.resource;

import com.jns.libraryapi.api.dto.BookDTO;
import com.jns.libraryapi.api.dto.LoanDTO;
import com.jns.libraryapi.api.model.entity.Book;
import com.jns.libraryapi.api.model.entity.Loan;
import com.jns.libraryapi.api.service.BookService;
import com.jns.libraryapi.api.service.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api("Book API")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService service;

    private final ModelMapper modelMapper;

    private final LoanService loanService;


    @ApiOperation("Find a Book By ID")
    @GetMapping("{id}")
    public BookDTO get(@PathVariable long id) {
        log.info("obtaining details for book id: {} ", id);
        return service.getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Book succesfully deleted")
    })
    @ApiOperation("Delete a Book By ID")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) throws Exception {
        log.info("Deletes a book by id: {} ", id);
        Book book = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        service.delete(book);

    }

    @ApiOperation("Create a Book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        log.info("Creating a book for isbn: {} ", dto.getIsbn());

        Book entity = modelMapper.map(dto, Book.class);

        entity = service.save(entity);

        return modelMapper.map(entity, BookDTO.class);
    }

    @PutMapping("{id}")
    public BookDTO update(@PathVariable long id, @RequestBody BookDTO dto) {
        log.info("Updating book of id: {} ", id);
        return service.getById(id).map(book -> {
            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());

            try {
                book = service.update(book);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return modelMapper.map(book, BookDTO.class);
        })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @ApiOperation("Find a Book By Params")
    @GetMapping
    public Page<BookDTO> find(BookDTO dto, Pageable pageable) {
        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = service.find(filter, pageable);
        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>(list, pageable, result.getTotalElements());
    }

    @GetMapping("{id}/loans")
    public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable) {
        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Page<Loan> result = loanService.getLoansByBook(book, pageable);

        List<LoanDTO> loanDTOS = result.getContent()
                .stream()
                .map(loan -> {
                    Book loanBook = loan.getBook();
                    BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                }).collect(Collectors.toList());

        return new PageImpl<>(loanDTOS, pageable, result.getTotalElements());
    }

}
