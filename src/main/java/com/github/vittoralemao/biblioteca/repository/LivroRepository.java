package com.github.vittoralemao.biblioteca.repository;

import com.github.vittoralemao.biblioteca.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
