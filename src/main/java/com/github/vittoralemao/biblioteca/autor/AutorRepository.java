package com.github.vittoralemao.biblioteca.autor;

import com.github.vittoralemao.biblioteca.nacionalidade.Nacionalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

    @EntityGraph(attributePaths = "livros")
    Optional<Autor> findById(UUID id);

    @Query("SELECT autor FROM Autor autor JOIN FETCH autor.livros WHERE autor.id = :id")
    Optional<Autor> buscarAutorComLivrosPorId(@Param("id") UUID id);

    List<Autor> findByNomeContainingIgnoreCase(String nome);
    List<Autor> findByNacionalidade_NomeContainingIgnoreCase(String nomeNacionalidade);
    List<Autor> findByNomeContainingIgnoreCaseAndNacionalidade_NomeContainingIgnoreCase(String nome, String nomeNacionalidade);

    boolean existsByNomeAndDataNascimentoAndNacionalidade(String nome, LocalDate dataNascimento, Nacionalidade nacionalidade);
    boolean existsByNomeAndDataNascimentoAndNacionalidadeAndIdNot(String nome, LocalDate dataNascimento, Nacionalidade nacionalidade, UUID id);

}
