package com.github.vittoralemao.biblioteca.modules.usuario;

import com.github.vittoralemao.biblioteca.share.EntidadeDuplicadaException;

public class UsuarioDuplicadoException extends EntidadeDuplicadaException {
    public UsuarioDuplicadoException(String message) {
        super(message);
    }
}
