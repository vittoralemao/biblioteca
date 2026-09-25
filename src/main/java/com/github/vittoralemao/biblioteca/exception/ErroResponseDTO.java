package com.github.vittoralemao.biblioteca.exception;

import java.util.List;

public record ErroResponseDTO(
        int status,
        String message,
        List<String> errors
) {
}
