package com.github.vittoralemao.biblioteca.livro;

import com.github.vittoralemao.biblioteca.autor.Autor;
import com.github.vittoralemao.biblioteca.categoria.Categoria;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor JOIN FETCH l.categoria WHERE l.id = :id")
    Optional<Livro> buscarLivroComAutorECategoria(@Param("id") UUID uuid);

    @Query("SELECT COUNT(l) FROM Livro l WHERE l.autor.id = :autorId")
    long contarLivrosPorAutor(@Param("autorId") UUID autorId);

    @Modifying
    @Query("DELETE FROM Livro l WHERE l.isbn IS NULL")
    int apagarLivrosSemIsbn();

    Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    Page<Livro> findByAutor_NomeContainingIgnoreCase(String autor, Pageable pageable);
    Page<Livro> findByCategoria_NomeContainingIgnoreCase(String categoria, Pageable pageable);

    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutor_NomeContainingIgnoreCase(String autor);
    List<Livro> findByCategoria_NomeContainingIgnoreCase(String categoria);
    List<Livro> findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCase(String titulo, String autor);
    List<Livro> findByTituloContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String titulo, String categoria);
    List<Livro> findByAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String autor, String categoria);
    List<Livro> findByTituloContainingIgnoreCaseAndAutor_NomeContainingIgnoreCaseAndCategoria_NomeContainingIgnoreCase(String titulo, String autor, String categoria);

    List<Livro> findAllByOrderByAnoPublicacaoDesc();
    List<Livro> findAllByOrderByTituloAsc();
    List<Livro> findTop5ByOrderByDataCadastroDesc();
    List<Livro> findByAnoPublicacaoGreaterThanEqual(Integer ano);
    List<Livro> findByAnoPublicacaoBetween(Integer anoInicio, Integer anoFim);
    List<Livro> findByIsbnIsNull();
    List<Livro> findByAnoPublicacaoIn(List<Integer> anos);

    boolean existsByAutor(Autor autor);
    boolean existsByCategoria(Categoria categoria);

    boolean existsByIsbn(@Nullable String isbn);
    boolean existsByIsbnAndIdNot(@Nullable String isbn, UUID id);
    boolean existsByTituloAndAutor(String titulo, Autor autor);
    boolean existsByTituloAndAutorAndIdNot(String titulo, Autor autor, UUID id);
}
