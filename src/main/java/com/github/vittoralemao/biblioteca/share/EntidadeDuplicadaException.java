package com.github.vittoralemao.biblioteca.share;

public abstract class EntidadeDuplicadaException extends RuntimeException {
    protected EntidadeDuplicadaException(String mensagem){
        super(mensagem);
    }
}
