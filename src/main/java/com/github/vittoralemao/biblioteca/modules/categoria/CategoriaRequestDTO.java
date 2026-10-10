package com.github.vittoralemao.biblioteca.modules.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO(

        @NotBlank(message = "O nome da categoria é obrigatório!")
        String nome
) {
}
