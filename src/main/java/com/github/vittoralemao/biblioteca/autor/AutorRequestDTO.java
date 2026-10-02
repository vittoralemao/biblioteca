package com.github.vittoralemao.biblioteca.autor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.UUID;

public record AutorRequestDTO(

        @NotBlank(message = "O nome do autor é obrigatório")
        String nome,

        @NotNull(message = "A data de nascimento é obrigatório")
        @Past(message = "A data de nascimento deve ser anterior à data atual")
        LocalDate dataNascimento,

        @NotNull(message = "Nacionalidade é obrigatório")
        UUID nacionalidadeId
) {}