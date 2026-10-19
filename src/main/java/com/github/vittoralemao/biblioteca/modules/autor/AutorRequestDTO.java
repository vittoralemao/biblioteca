package com.github.vittoralemao.biblioteca.modules.autor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorRequestDTO(

        @NotBlank(message = "O nome do autor é obrigatório")
        @Size(max = 100, message = "O nome do autor deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "A data de nascimento é obrigatório")
        @Past(message = "A data de nascimento deve ser anterior à data atual")
        LocalDate dataNascimento,

        @NotNull(message = "Nacionalidade é obrigatório")
        UUID nacionalidadeId
) {}