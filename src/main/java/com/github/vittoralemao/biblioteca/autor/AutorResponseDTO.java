package com.github.vittoralemao.biblioteca.autor;

import com.github.vittoralemao.biblioteca.nacionalidade.NacionalidadeResponseDTO;

import java.time.LocalDate;
import java.util.UUID;

public record AutorResponseDTO(

        UUID id,
        String nome,
        LocalDate dataNascimento,
        NacionalidadeResponseDTO nacionalidade

) {
    public static AutorResponseDTO fromEntity(Autor autor) {
        return new AutorResponseDTO(
                autor.getId(),
                autor.getNome(),
                autor.getDataNascimento(),
                new NacionalidadeResponseDTO(
                        autor.getNacionalidade().getId(),
                        autor.getNacionalidade().getNome(),
                        autor.getNacionalidade().getIso()
                )
        );
    }

}
