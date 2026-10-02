package com.github.vittoralemao.biblioteca.nacionalidade;

import jakarta.validation.constraints.NotBlank;

public record NacionalidadeRequestDTO(

        @NotBlank(message = "O nome da nacionalidade é obrigatório!")
        String nome,

        @NotBlank(message = "O ISO da nacionalidade é obrigatório!")
        String iso
) {
}
