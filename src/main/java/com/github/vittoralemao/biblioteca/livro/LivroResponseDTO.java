package com.github.vittoralemao.biblioteca.livro;

import com.github.vittoralemao.biblioteca.autor.AutorResponseDTO;
import com.github.vittoralemao.biblioteca.categoria.CategoriaResponseDTO;
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
