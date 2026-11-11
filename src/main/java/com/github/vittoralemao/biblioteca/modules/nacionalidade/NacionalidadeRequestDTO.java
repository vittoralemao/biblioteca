package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Locale;

public record NacionalidadeRequestDTO(

        @NotBlank(message = "O nome da nacionalidade é obrigatório!")
        @Size(max = 60, message = "O nome da nacionalidade deve ter no máximo 60 caracteres")
        String nome,

        @NotBlank(message = "O ISO da nacionalidade é obrigatório!")
        @Pattern(regexp = "[A-Z]{3}", message = "O ISO da nacionalidade deve ter exatamente 3 letras")
        String iso
) {
        public NacionalidadeRequestDTO {
                if (nome != null) {
                        nome = nome.strip();
                }
                if (iso != null) {
                        iso = iso.strip().toUpperCase(Locale.ROOT);
                }
        }
}
