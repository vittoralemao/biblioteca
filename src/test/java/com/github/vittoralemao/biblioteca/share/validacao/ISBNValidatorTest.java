package com.github.vittoralemao.biblioteca.share.validacao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ISBNValidatorTest {

    private final ISBNValidator validator = new ISBNValidator();

    @ParameterizedTest
    @ValueSource(strings = {"9788535914849", "8535914846", "080442957X"})
    void deveAceitarIsbnValido(String isbn) {
        assertThat(validator.isValid(isbn, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"9788535914840", "8535914845", "", "123", "97885359148AB", "X535914846"})
    void deveRejeitarIsbnInvalido(String isbn) {
        assertThat(validator.isValid(isbn, null)).isFalse();
    }

    @Test
    void deveAceitarIsbnNuloPorqueEOpcional() {
        assertThat(validator.isValid(null, null)).isTrue();
    }
}
