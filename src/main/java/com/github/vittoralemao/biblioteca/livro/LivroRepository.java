package com.github.vittoralemao.biblioteca.livro;

import com.github.vittoralemao.biblioteca.autor.Autor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutor_NomeContainingIgnoreCase(String autor);
    List<Livro> findByCategoria_NomeContainingIgnoreCase(String categoria);
    List<Livro> findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCase(String titulo, String autor);
    List<Livro> findByTituloContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String titulo, String categoria);
    List<Livro> findByAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String autor, String categoria);
    List<Livro> findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String titulo, String autor, String categoria);

    boolean existsByIsbn(@Nullable String isbn);
    boolean existsByIsbnAndIdNot(@Nullable String isbn, UUID id);
    boolean existsByTituloAndAutor(String titulo, Autor autor);
    boolean existsByTituloAndAutorAndIdNot(String titulo, Autor autor, UUID id);
}
