package com.github.vittoralemao.biblioteca.exception;

public abstract class EntidadeDuplicadaException extends RuntimeException {
    protected EntidadeDuplicadaException(String mensagem){
        super(mensagem);
    }
}
