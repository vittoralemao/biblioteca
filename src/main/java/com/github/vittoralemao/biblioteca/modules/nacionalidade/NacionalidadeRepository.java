package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NacionalidadeRepository extends JpaRepository <Nacionalidade, UUID> {

    List<Nacionalidade> findByNomeContainingIgnoreCase(String nome);
    List<Nacionalidade> findByIsoContainingIgnoreCase(String iso);

    boolean existsByNome(String nome);
    boolean existsByIso(String iso);

    boolean existsByNomeAndIdNot(String nome, UUID id);
    boolean existsByIsoAndIdNot(String iso, UUID id);

}
