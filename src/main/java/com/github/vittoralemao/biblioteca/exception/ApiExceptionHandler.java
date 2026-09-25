package com.github.vittoralemao.biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(AutorNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> trataAutorNaoEncontrado(AutorNaoEncontradoException ex){

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
}
