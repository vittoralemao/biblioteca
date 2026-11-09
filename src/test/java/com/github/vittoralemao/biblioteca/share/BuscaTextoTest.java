package com.github.vittoralemao.biblioteca.share;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class BuscaTextoTest {

    @ParameterizedTest(name = "{0} vira {1}")
    @CsvSource({
            "java, %java%",
            "JAVA, %java%",
            "100%, %100\\%%",
            "a_b, %a\\_b%",
            "%, %\\%%",
            "_, %\\_%",
            "c:\\pasta, %c:\\\\pasta%"
    })
    void deveMontarPadraoContemComEscape(String termo, String esperado) {
        assertThat(BuscaTexto.contem(termo)).isEqualTo(esperado);
    }
}
