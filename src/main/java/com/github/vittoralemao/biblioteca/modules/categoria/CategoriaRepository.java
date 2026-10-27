package com.github.vittoralemao.biblioteca.modules.categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    Page<Categoria> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, UUID id);
}
