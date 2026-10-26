package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.modules.autor.Autor;
import com.github.vittoralemao.biblioteca.modules.categoria.Categoria;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor a JOIN FETCH a.nacionalidade JOIN FETCH l.categoria WHERE l.id = :id")
    Optional<Livro> buscarLivroComAutorECategoria(@Param("id") UUID uuid);

    @Override
    @EntityGraph(attributePaths = {"autor", "autor.nacionalidade", "categoria"})
    Page<Livro> findAll(Specification<Livro> spec, Pageable pageable);

    boolean existsByAutor(Autor autor);

    boolean existsByCategoria(Categoria categoria);

    boolean existsByIsbn(@Nullable String isbn);

    boolean existsByIsbnAndIdNot(@Nullable String isbn, UUID id);

    boolean existsByTituloAndAutor(String titulo, Autor autor);

    boolean existsByTituloAndAutorAndIdNot(String titulo, Autor autor, UUID id);
}
