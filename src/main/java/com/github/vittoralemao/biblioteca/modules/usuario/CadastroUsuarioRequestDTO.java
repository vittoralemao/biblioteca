package com.github.vittoralemao.biblioteca.modules.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroUsuarioRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O nome do usuário deve ter no máximo 255 caracteres")
        String nome,

        @NotBlank(message = "O login é obrigatório")
        @Size(max = 255, message = "O login do usuário deve ter no máximo 255 caracteres")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
        String senha,

        @NotNull(message = "O papel é obrigatório")
        Papel papel
) {}
