package com.github.vittoralemao.biblioteca.share;

import java.util.Locale;

public final class BuscaTexto {

    public static final char ESCAPE = '\\';

    private BuscaTexto() {
    }

    public static String contem(String termo) {
        String escapado = termo.toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");

        return "%" + escapado + "%";

    }
}
