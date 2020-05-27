package com.jns.libraryapi.api.service;

import com.jns.libraryapi.api.dto.LoanFilterDTO;
import com.jns.libraryapi.api.exception.BussinessException;
import com.jns.libraryapi.api.model.entity.Book;
import com.jns.libraryapi.api.model.entity.Loan;
import com.jns.libraryapi.api.model.repository.LoanRepository;
import com.jns.libraryapi.api.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService service;

    @MockBean
    LoanRepository repository;

    @BeforeEach
    public void setup() {
        this.service = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void saveLoanTest() {
        // Cenário

        Loan loan = createLoan();

        Loan loanSave = createLoan();
        loanSave.setId(1L);

        when(repository.existsByBookAndNotReturned(loan.getBook())).thenReturn(false);

        when(repository.save(loan)).thenReturn(loanSave);


        // Execução
        Loan savedLoan = service.save(loan);

        // Verificação
        assertThat(savedLoan.getId()).isNotNull();
        assertThat(savedLoan.getCustumer()).isEqualTo(loanSave.getCustumer());
        assertThat(savedLoan.getBook().getId()).isEqualTo(loanSave.getBook().getId());
        assertThat(savedLoan.getLoanDate()).isEqualTo(loanSave.getLoanDate());

    }


    @Test
    @DisplayName("Deve lançar erro de negocio ao salvar um emprestimo com o livro já emprestado")
    public void loaneBookSaveTest() {
        Book book = Book.builder().id(1L).build();

        String custumer = "Fulano";

        Loan salvingLoan = createLoan();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.save(salvingLoan));

        assertThat(exception)
                .isInstanceOf(BussinessException.class)
                .hasMessage("Book already loaned");

        verify(repository, never()).save(salvingLoan);
    }

    @Test
    @DisplayName("Deve obter as informações de um emprestado por id")
    public void getLoadDetailsId() {

        Long id = 1L;

        Loan loan = createLoan();
        loan.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(loan));

        // Execução
        Optional<Loan> result = service.getById(id);

        // Verificação
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCustumer()).isEqualTo(loan.getCustumer());
        assertThat(result.get().getBook().getId()).isEqualTo(loan.getBook().getId());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        verify(repository).findById(id);

    }

    @Test
    @DisplayName("Deve atualizar um emprestimo")
    public void updateLoanTest() {
        Loan loan = createLoan();
        loan.setId(1l);
        loan.setReturned(true);

        when(repository.save(loan)).thenReturn(loan);

        Loan updatedLoan = service.update(loan);

        assertThat(updatedLoan.getReturned()).isTrue();
        verify(repository).save(loan);

    }

    @Test
    @DisplayName("Deve filtrar emprestimos pelas propriedade")
    public void findLoanTest() {

        // Cenário

        LoanFilterDTO filterDTO = LoanFilterDTO.builder().isbn("321").custumer("Fulano").build();

        Loan loan = createLoan();
        loan.setId(1l);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> list = Arrays.asList(loan);

        Page<Loan> page = new PageImpl<>(list, pageRequest, list.size());

        when(repository.findByBookIsbnOrCustomer(Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(page);

        // Execução
        Page<Loan> result = service.find(filterDTO, pageRequest);

        // Verificações
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    public static Loan createLoan() {
        Book book = Book.builder().id(1L).build();

        String custumer = "Fulano";

        return Loan.builder()
                .book(book)
                .custumer(custumer)
                .loanDate(LocalDate.now())
                .build();
    }
}
