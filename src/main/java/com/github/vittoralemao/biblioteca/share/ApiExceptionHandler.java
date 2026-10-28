package com.github.vittoralemao.biblioteca.share;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail trataEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        return TipoProblema.RECURSO_NAO_ENCONTRADO.criar(ex.getMessage());
    }

    @ExceptionHandler(EntidadeDuplicadaException.class)
    public ProblemDetail trataEntidadeDuplicada(EntidadeDuplicadaException ex) {
        return TipoProblema.RECURSO_DUPLICADO.criar(ex.getMessage());
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ProblemDetail trataEntidadeEmUso(EntidadeEmUsoException ex) {
        return TipoProblema.RECURSO_EM_USO.criar(ex.getMessage());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail trataCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        return TipoProblema.CREDENCIAIS_INVALIDAS.criar(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail trataViolacaoDeIntegridade(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade no banco: {}", ex.getMostSpecificCause().getMessage());
        return TipoProblema.CONFLITO_DE_DADOS.criar("A operação conflita com dados já existentes.");
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ProblemDetail trataCampoDeOrdenacaoInvalido(PropertyReferenceException ex) {
        return TipoProblema.ORDENACAO_INVALIDA.criar("Campo de ordenação inválido: " + ex.getPropertyName());
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        ProblemDetail problema = TipoProblema.DADOS_INVALIDOS.criar("A requisição tem campos inválidos.");
        problema.setProperty("errors", erros);

        return ResponseEntity.status(problema.getStatus()).body(problema);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.debug("Corpo da requisição inválido: {}", ex.getMessage());

        ProblemDetail problema = TipoProblema.CORPO_INVALIDO.criar("Corpo da requisição inválido ou mal formatado.");
        return ResponseEntity.status(problema.getStatus()).body(problema);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail problema = TipoProblema.RECURSO_NAO_ENCONTRADO.criar("Rota não encontrada: /" + ex.getResourcePath());
        return ResponseEntity.status(problema.getStatus()).body(problema);
    }
}
