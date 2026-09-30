package com.github.vittoralemao.biblioteca.dto;

import com.github.vittoralemao.biblioteca.entity.Categoria;

import java.util.UUID;

public record CategoriaResponseDTO(
        UUID id,
        String nome
) {
    public static CategoriaResponseDTO fromEntity(Categoria categoria){
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome()
        );
    }
}
