package com.github.vittoralemao.biblioteca.modules.autor;

import com.github.vittoralemao.biblioteca.share.EntidadeNaoEncontradaException;

public class AutorNaoEncontradoException extends EntidadeNaoEncontradaException {

    public AutorNaoEncontradoException(String message) {
        super(message);
    }
}
