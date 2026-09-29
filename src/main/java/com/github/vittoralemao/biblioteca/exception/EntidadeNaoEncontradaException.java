package com.github.vittoralemao.biblioteca.exception;

public abstract class EntidadeNaoEncontradaException extends RuntimeException{
    protected EntidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}
