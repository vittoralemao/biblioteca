package com.github.vittoralemao.biblioteca.repository;

import com.github.vittoralemao.biblioteca.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT autor FROM Autor autor JOIN FETCH autor.livros WHERE autor.id = :id")
    Optional<Autor> buscarAutorComLivrosPorId(@Param("id") Long id);

}
