package com.jns.libraryapi.api.model.repository;

import com.jns.libraryapi.api.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro com Isbn informado")
    public void returnTrueWhenIsbnExists() {
        // Cenário
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        // Execução
        boolean exists = repository.existsByIsbn(isbn);

        // Verificação
        assertThat(exists).isTrue();
    }


    @Test
    @DisplayName("Deve retornar falso quando não existir um livro com Isbn informado")
    public void returnFalseWhenIsbnDoesntExists() {
        // Cenário
        String isbn = "123";

        // Execução
        boolean exists = repository.existsByIsbn(isbn);

        // Verificação
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro ")
    public void findById() {
        // Cenário
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        // Execução
        Optional<Book> foubdBook = repository.findById(book.getId());

        // Verificação
        assertThat(foubdBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void save() {

        // Cenário
        Book book = createNewBook("123");

        Book saveBook = repository.save(book);

        assertThat(saveBook.getId()).isNotNull();

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void delete() {

        // Cenário
        Book book = createNewBook("123");
        book = entityManager.persist(book);

        repository.delete(book);

        Optional<Book> foubdBook = repository.findById(book.getId());

        assertThat(foubdBook.isPresent()).isFalse();
    }



    public static Book createNewBook(String isbn) {
        return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
    }
}
