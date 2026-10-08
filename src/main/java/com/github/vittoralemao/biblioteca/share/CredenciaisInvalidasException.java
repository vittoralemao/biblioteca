package com.github.vittoralemao.biblioteca.share;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Login ou senha inválidos.");
    }
}
