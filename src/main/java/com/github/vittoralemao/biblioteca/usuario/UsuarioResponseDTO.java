package com.github.vittoralemao.biblioteca.usuario;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String login,
        Papel papel
) {
}
