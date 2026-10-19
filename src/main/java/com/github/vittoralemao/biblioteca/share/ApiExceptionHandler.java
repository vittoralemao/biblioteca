package com.github.vittoralemao.biblioteca.share;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> trataErroDeValidacao(MethodArgumentNotValidException ex){

        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> trataCorpoInvalido(HttpMessageNotReadableException ex) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Corpo da requisição inválido ou mal formatado.",
                List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponseDTO> trataViolacaoDeIntegridade(DataIntegrityViolationException ex) {

        log.warn("Violação de integridade no banco: {}", ex.getMostSpecificCause().getMessage());

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.CONFLICT.value(),
                "A operação conflita com dados já existentes.",
                List.of()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }



}
