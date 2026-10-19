package com.github.vittoralemao.biblioteca.modules.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank(message = "O login é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Size(max = 72, message = "A senha deve ter no máximo 72 caracteres")
        String senha
) {
}
