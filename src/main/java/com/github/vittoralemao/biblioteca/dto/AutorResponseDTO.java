package com.github.vittoralemao.biblioteca.dto;

import com.github.vittoralemao.biblioteca.entity.Autor;
import com.github.vittoralemao.biblioteca.enums.Nacionalidade;

import java.time.LocalDate;
import java.util.UUID;

public record AutorResponseDTO(

        UUID id,
        String nome,
        LocalDate dataNascimento,
        Nacionalidade nacionalidade

) {
    public static AutorResponseDTO fromEntity(Autor autor) {
        return new AutorResponseDTO(
                autor.getId(),
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade()
        );
    }

}
