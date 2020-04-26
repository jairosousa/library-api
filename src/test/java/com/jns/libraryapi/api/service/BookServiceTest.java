package com.jns.libraryapi.api.service;

import com.jns.libraryapi.api.exception.BussinessException;
import com.jns.libraryapi.api.model.entity.Book;
import com.jns.libraryapi.api.model.repository.BookRepository;
import com.jns.libraryapi.api.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setup() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        // Cenário
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book))
                .thenReturn(
                        Book.builder().id(1l).author("Fulano").title("As aventuras").isbn("123").build()
                );

        // Execução
        Book saveBook = service.save(book);

        // Verificação
        assertThat(saveBook.getId()).isNotNull();
        assertThat(saveBook.getIsbn()).isEqualTo("123");

        assertThat(saveBook.getTitle()).isEqualTo("As aventuras");
        assertThat(saveBook.getAuthor()).isEqualTo("Fulano");
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shoulNotSaveABookDuplicadedISBN() {

        // Cenário
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        // Execução
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        // Verificações
        assertThat(exception)
                .isInstanceOf(BussinessException.class)
                .hasMessage("Isbn já cadastrado");

        Mockito.verify(repository, Mockito.never()).save(book);

    }

    @Test
    @DisplayName("Deve obter livro por id")
    public void getById() {

        // Cenário
        Long id = 1l;
        Book book = createValidBook();
        book.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        // Execução
        Optional<Book> foundBook = service.getById(id);

        // Verificações
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());

    }

    @Test
    @DisplayName("Deve retornat vazio ao obter um livro por id quando ele não exitir na base")
    public void bookNotFoundById() {

        // Cenário
        Long id = 1l;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        // Execução
        Optional<Book> book = service.getById(id);

        // Verificações
        assertThat(book.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() {

        // Cenário
        Book book = Book.builder().id(1l).build();

        // Execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book)); // verifica que não lança nenhuma exceção

        // Verificações
        Mockito.verify(repository, Mockito.times(1)).delete(book);

    }

    @Test
    @DisplayName("Deve ocorrer um erro ao tentar deletar um livro inexistente")
    public void deleteInvalidBookTest() {

        // Cenário
        Book book = new Book();

        // Execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        // Verificações
        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() throws Exception {

        // Cenário
        long id = 1l;
        Book updatingBook = Book.builder().id(id).build();

        Book updateBook = createValidBook();
        updateBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updateBook);


        // Execução
        Book book = service.update(updatingBook);

        // Verificações
        assertThat(book.getId()).isEqualTo(id);
        assertThat(book.getAuthor()).isEqualTo(updateBook.getAuthor());
        assertThat(book.getTitle()).isEqualTo(updateBook.getTitle());
        assertThat(book.getIsbn()).isEqualTo(updateBook.getIsbn());

    }

    @Test
    @DisplayName("Deve ocorrer um erro ao tentar atualizar um livro inexistente")
    public void updateInvalidBookTest() {

        // Cenário
        Book book = new Book();

        // Execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

        // Verificações
        Mockito.verify(repository, Mockito.never()).save(book);
    }


    @Test
    @DisplayName("Deve filtrar livros pelas propriedade")
    public void findBookTest() {

        // Cenário
        Book book = createValidBook();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Book> list = Arrays.asList(book);

        Page<Book> page = new PageImpl<>(list, pageRequest, 1);

        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        // Execução
        Page<Book> result = service.find(book, pageRequest);

        // Verificações
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }


    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

}