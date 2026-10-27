package com.github.vittoralemao.biblioteca.share.validacao;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jspecify.annotations.Nullable;

public class ISBNValidator implements ConstraintValidator<ISBN, String> {

    @Override
    public boolean isValid(@Nullable String isbn, ConstraintValidatorContext context) {
        if (isbn == null) {
            return true;
        }
        return switch (isbn.length()) {
            case 13 -> isbn13Valido(isbn);
            case 10 -> isbn10Valido(isbn);
            default -> false;
        };
    }

    private boolean isbn13Valido(String isbn) {
        int soma = 0;
        for (int i = 0; i < 13; i++) {
            char c = isbn.charAt(i);
            if (!ehDigito(c)) {
                return false;
            }
            int peso = (i % 2 == 0) ? 1 : 3;
            soma += (c - '0') * peso;
        }
        return soma % 10 == 0;
    }

    private boolean isbn10Valido(String isbn) {
        int soma = 0;
        for (int i = 0; i < 10; i++) {
            char c = isbn.charAt(i);
            int valor;
            if (ehDigito(c)) {
                valor = c - '0';
            } else if (c == 'X' && i == 9) {
                valor = 10;
            } else {
                return false;
            }
            soma += valor * (10 - i);
        }
        return soma % 11 == 0;
    }

    private boolean ehDigito(char c) {
        return c >= '0' && c <= '9';
    }
}
