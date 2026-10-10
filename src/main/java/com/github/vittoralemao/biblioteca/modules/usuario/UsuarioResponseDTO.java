package com.github.vittoralemao.biblioteca.modules.usuario;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String login,
        Papel papel
) {
}
