package com.github.vittoralemao.biblioteca.repository;

import com.github.vittoralemao.biblioteca.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
