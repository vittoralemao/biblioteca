package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.modules.autor.Autor;
import com.github.vittoralemao.biblioteca.modules.categoria.Categoria;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor a JOIN FETCH a.nacionalidade JOIN FETCH l.categoria WHERE l.id = :id")
    Optional<Livro> buscarLivroComAutorECategoria(@Param("id") UUID uuid);

    @Override
    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    List<Livro> findAll();

    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    List<Livro> findByAutor_NomeContainingIgnoreCase(String autor);

    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    List<Livro> findByCategoria_NomeContainingIgnoreCase(String categoria);

    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    List<Livro> findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCase(String titulo, String autor);

    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    List<Livro> findByTituloContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String titulo, String categoria);

    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    List<Livro> findByAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String autor, String categoria);

    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    List<Livro> findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String titulo, String autor, String categoria);

    boolean existsByAutor(Autor autor);

    boolean existsByCategoria(Categoria categoria);

    boolean existsByIsbn(@Nullable String isbn);

    boolean existsByIsbnAndIdNot(@Nullable String isbn, UUID id);

    boolean existsByTituloAndAutor(String titulo, Autor autor);

    boolean existsByTituloAndAutorAndIdNot(String titulo, Autor autor, UUID id);
}
