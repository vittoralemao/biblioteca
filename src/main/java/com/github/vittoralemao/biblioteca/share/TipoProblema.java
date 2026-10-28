package com.github.vittoralemao.biblioteca.share;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public enum TipoProblema {

    RECURSO_NAO_ENCONTRADO("recurso-nao-encontrado", "Recurso não encontrado", HttpStatus.NOT_FOUND),
    RECURSO_DUPLICADO("recurso-duplicado", "Recurso duplicado", HttpStatus.CONFLICT),
    RECURSO_EM_USO("recurso-em-uso", "Recurso em uso", HttpStatus.CONFLICT),
    CONFLITO_DE_DADOS("conflito-de-dados", "Conflito de dados", HttpStatus.CONFLICT),
    DADOS_INVALIDOS("dados-invalidos", "Dados inválidos", HttpStatus.BAD_REQUEST),
    CORPO_INVALIDO("corpo-invalido", "Corpo da requisição inválido", HttpStatus.BAD_REQUEST),
    ORDENACAO_INVALIDA("ordenacao-invalida", "Ordenação inválida", HttpStatus.BAD_REQUEST),
    CREDENCIAIS_INVALIDAS("credenciais-invalidas", "Credenciais inválidas", HttpStatus.UNAUTHORIZED),
    AUTENTICACAO_NECESSARIA("autenticacao-necessaria", "Autenticação necessária", HttpStatus.UNAUTHORIZED),
    ACESSO_NEGADO("acesso-negado", "Acesso negado", HttpStatus.FORBIDDEN);

    private static final String BASE = "https://biblioteca.vitoralemao.com.br/problemas/";

    private final String slug;
    private final String titulo;
    private final HttpStatus status;

    TipoProblema(String slug, String titulo, HttpStatus status) {
        this.slug = slug;
        this.titulo = titulo;
        this.status = status;
    }

    public ProblemDetail criar(String detalhe) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setType(URI.create(BASE + slug));
        problema.setTitle(titulo);
        return problema;
    }
}
