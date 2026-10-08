package com.github.vittoralemao.biblioteca.share;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> trataEntidadeNaoEncontrado(EntidadeNaoEncontradaException ex){

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(EntidadeDuplicadaException.class)
    public ResponseEntity<ErroResponseDTO> trataEntidadeDuplicada(EntidadeDuplicadaException ex){

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }
    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<ErroResponseDTO> trataEntidadeEmUso(EntidadeEmUsoException ex){

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroResponseDTO> trataCredenciaisInvalidas(CredenciaisInvalidasException e){

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                List.of());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }



}
