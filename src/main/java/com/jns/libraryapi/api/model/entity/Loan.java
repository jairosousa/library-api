package com.jns.libraryapi.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(length = 100)
    private String custumer;

    @Column(name = "custumer_email")
    private String custumerEmail;

    @JoinColumn(name = "book_id")
    @ManyToOne
    private Book book;

    @Column
    private LocalDate loanDate;

    @Column
    private Boolean returned;
}
