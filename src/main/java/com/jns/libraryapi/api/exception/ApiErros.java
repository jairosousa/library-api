package com.jns.libraryapi.api.exception;

import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErros {
    List<String> errors;

    public ApiErros(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
    }

    public ApiErros(BussinessException ex) {
        this.errors = Arrays.asList(ex.getMessage());
    }

    public ApiErros(ResponseStatusException ex) {
        this.errors = Arrays.asList(ex.getReason());

    }

    public List<String> getErrors() {
        return errors;
    }
}
