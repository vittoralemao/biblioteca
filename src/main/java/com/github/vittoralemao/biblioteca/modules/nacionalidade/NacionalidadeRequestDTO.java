package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NacionalidadeRequestDTO(

        @NotBlank(message = "O nome da nacionalidade é obrigatório!")
        @Size(max = 60, message = "O nome da nacionalidade deve ter no máximo 60 caracteres")
        String nome,

        @NotBlank(message = "O ISO da nacionalidade é obrigatório!")
        @Size(min = 3, max = 3, message = "O ISO da nacionalidade deve ter exatamente 3 caracteres")
        String iso
) {
}
