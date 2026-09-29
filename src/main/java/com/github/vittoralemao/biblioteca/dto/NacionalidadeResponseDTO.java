package com.github.vittoralemao.biblioteca.dto;

import com.github.vittoralemao.biblioteca.entity.Nacionalidade;

import java.util.UUID;

public record NacionalidadeResponseDTO(
        UUID id,
        String nome,
        String iso
) {
    public static NacionalidadeResponseDTO fromEntity(Nacionalidade nacionalidade) {
        return new NacionalidadeResponseDTO(
                nacionalidade.getId(),
                nacionalidade.getNome(),
                nacionalidade.getIso()
        );
    }
}
