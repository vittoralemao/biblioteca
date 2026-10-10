package com.github.vittoralemao.biblioteca.modules.usuario;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String login,
        @NotBlank String senha
) {
}
