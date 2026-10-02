package com.github.vittoralemao.biblioteca.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LivroRequestDTO(
        @NotBlank(message = "O título do livro é obrigatório")
        String titulo,

        @NotNull(message = "O autor é obrigatório")
        UUID autorId,

        @NotNull(message = "A categoria é obrigatória")
        UUID categoriaId,

        String isbn,

        Integer anoPublicacao
) {
}
