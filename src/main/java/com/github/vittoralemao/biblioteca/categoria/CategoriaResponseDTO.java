package com.github.vittoralemao.biblioteca.categoria;

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
