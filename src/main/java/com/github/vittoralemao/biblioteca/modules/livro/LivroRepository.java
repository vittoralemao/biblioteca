package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.modules.autor.Autor;
import com.github.vittoralemao.biblioteca.modules.categoria.Categoria;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor JOIN FETCH l.categoria WHERE l.id = :id")
    Optional<Livro> buscarLivroComAutorECategoria(@Param("id") UUID uuid);

    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutor_NomeContainingIgnoreCase(String autor);
    List<Livro> findByCategoria_NomeContainingIgnoreCase(String categoria);
    List<Livro> findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCase(String titulo, String autor);
    List<Livro> findByTituloContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String titulo, String categoria);
    List<Livro> findByAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String autor, String categoria);
    List<Livro> findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String titulo, String autor, String categoria);
    
    boolean existsByAutor(Autor autor);
    boolean existsByCategoria(Categoria categoria);

    boolean existsByIsbn(@Nullable String isbn);
    boolean existsByIsbnAndIdNot(@Nullable String isbn, UUID id);
    boolean existsByTituloAndAutor(String titulo, Autor autor);
    boolean existsByTituloAndAutorAndIdNot(String titulo, Autor autor, UUID id);
}
