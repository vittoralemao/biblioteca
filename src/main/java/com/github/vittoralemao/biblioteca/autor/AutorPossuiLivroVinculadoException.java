package com.github.vittoralemao.biblioteca.autor;

import com.github.vittoralemao.biblioteca.share.EntidadeEmUsoException;

public class AutorPossuiLivroVinculadoException extends EntidadeEmUsoException {
    public AutorPossuiLivroVinculadoException(String message) {
        super(message);
    }
}
