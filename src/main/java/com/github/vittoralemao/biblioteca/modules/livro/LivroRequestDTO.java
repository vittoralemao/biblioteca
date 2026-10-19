package com.github.vittoralemao.biblioteca.modules.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record LivroRequestDTO(
        @NotBlank(message = "O título do livro é obrigatório")
        @Size(max = 150, message = "O título do livro deve ter no máximo 150 caracteres")
        String titulo,

        @NotNull(message = "O autor é obrigatório")
        UUID autorId,

        @NotNull(message = "A categoria é obrigatória")
        UUID categoriaId,

        @Nullable
        @Size(max = 20, message = "O isbn deve ter no máximo 20 caracteres")
        String isbn,

        @Nullable
        Integer anoPublicacao
) {
}
