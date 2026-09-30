package com.github.vittoralemao.biblioteca.repository;

import com.github.vittoralemao.biblioteca.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    List<Categoria> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, UUID id);
}
