package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.modules.autor.AutorResponseDTO;
import com.github.vittoralemao.biblioteca.modules.categoria.CategoriaResponseDTO;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record LivroResponseDTO(
        UUID id,
        String titulo,
        AutorResponseDTO autor,
        CategoriaResponseDTO categoria,
        @Nullable String isbn,
        Integer anoPublicacao
) {
    public static LivroResponseDTO fromEntity(Livro livro){
      return new LivroResponseDTO(
              livro.getId(),
              livro.getTitulo(),
              AutorResponseDTO.fromEntity(livro.getAutor()),
              CategoriaResponseDTO.fromEntity(livro.getCategoria()),
              livro.getIsbn(),
              livro.getAnoPublicacao()
      );
    }
}
