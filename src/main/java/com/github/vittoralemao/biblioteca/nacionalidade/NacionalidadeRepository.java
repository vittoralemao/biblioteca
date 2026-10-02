package com.github.vittoralemao.biblioteca.nacionalidade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NacionalidadeRepository extends JpaRepository <Nacionalidade, UUID> {

    List<Nacionalidade> findByNomeContainingIgnoreCase(String nome);
    List<Nacionalidade> findByIsoContainingIgnoreCase(String iso);

    boolean existsByNomeAndIso(String nome, String iso);
    boolean existsByNomeAndIsoAndIdNot(String nome, String iso, UUID id);
}
