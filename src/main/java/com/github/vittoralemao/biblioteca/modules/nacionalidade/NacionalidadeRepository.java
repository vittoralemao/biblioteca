package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface NacionalidadeRepository extends JpaRepository<Nacionalidade, UUID>, JpaSpecificationExecutor<Nacionalidade> {

    boolean existsByNome(String nome);
    boolean existsByIso(String iso);

    boolean existsByNomeAndIdNot(String nome, UUID id);
    boolean existsByIsoAndIdNot(String iso, UUID id);

}
