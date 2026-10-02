package com.github.vittoralemao.biblioteca.share;

public abstract class EntidadeNaoEncontradaException extends RuntimeException{
    protected EntidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}
