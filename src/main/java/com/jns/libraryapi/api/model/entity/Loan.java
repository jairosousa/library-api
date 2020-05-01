package com.jns.libraryapi.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    private Long id;
    private String custumer;
    private Book book;
    private LocalDate loaanDate;
    private Boolean returned;
}
