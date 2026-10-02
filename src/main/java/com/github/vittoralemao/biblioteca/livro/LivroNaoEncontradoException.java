package com.github.vittoralemao.biblioteca.livro;

import com.github.vittoralemao.biblioteca.share.EntidadeNaoEncontradaException;

public class LivroNaoEncontradoException extends EntidadeNaoEncontradaException {
    public LivroNaoEncontradoException(String message) {
        super(message);
    }
}
