package com.github.vittoralemao.biblioteca.modules.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroUsuarioRequestDTO(
        @NotBlank String nome,
        @NotBlank String login,
        @NotBlank String senha,
        @NotNull Papel papel
) {}
