package com.github.vittoralemao.biblioteca.autor;

import com.github.vittoralemao.biblioteca.share.EntidadeNaoEncontradaException;

public class AutorNaoEncontradoException extends EntidadeNaoEncontradaException {

    public AutorNaoEncontradoException(String message) {
        super(message);
    }
}
