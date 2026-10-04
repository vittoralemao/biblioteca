package com.github.vittoralemao.biblioteca.categoria;

import com.github.vittoralemao.biblioteca.share.EntidadeEmUsoException;

public class CategoriaComLivroVinculadoException extends EntidadeEmUsoException {
    public CategoriaComLivroVinculadoException(String message) {
        super(message);
    }
}
