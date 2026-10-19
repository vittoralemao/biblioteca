package com.github.vittoralemao.biblioteca.modules.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(

        @NotBlank(message = "O nome da categoria é obrigatório!")
        @Size(max = 25, message = "O nome da categoria deve ter no máximo 25 caracteres")
        String nome
) {
}
