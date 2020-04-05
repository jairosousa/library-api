package com.jns.libraryapi.api.resource;

import com.jns.libraryapi.api.dto.BookDTO;
import com.jns.libraryapi.api.exception.ApiErros;
import com.jns.libraryapi.api.exception.BussinessException;
import com.jns.libraryapi.api.model.entity.Book;
import com.jns.libraryapi.api.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    private final ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable long id) {
        Book book = service.getById(id).get();
        return modelMapper.map(book, BookDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);

        entity = service.save(entity);

        return  modelMapper.map(entity, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErros(bindingResult);
    }

    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleBussinessException(BussinessException ex) {
        return new ApiErros(ex);
    }
}
