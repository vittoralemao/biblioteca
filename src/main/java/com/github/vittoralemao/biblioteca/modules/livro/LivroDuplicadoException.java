package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.share.EntidadeDuplicadaException;

public class LivroDuplicadoException extends EntidadeDuplicadaException {
    public LivroDuplicadoException(String message) {
        super(message);
    }
}
