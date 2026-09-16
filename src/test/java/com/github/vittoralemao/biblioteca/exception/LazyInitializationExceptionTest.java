package com.github.vittoralemao.biblioteca.exception;

import com.github.vittoralemao.biblioteca.entity.Autor;
import com.github.vittoralemao.biblioteca.entity.Livro;
import com.github.vittoralemao.biblioteca.enums.Nacionalidade;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LazyInitializationExceptionTest {

    private static final String NOME_TESTE = "George Orwell";

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void deveLancarExcecaoAoAcessarListaLazyForaDaSessao() {
        Autor autor = new Autor();
        autor.setNome(NOME_TESTE);
        autor.setNacionalidade(Nacionalidade.BRITANICO);
        Autor autorPersistido = entityManager.persistAndFlush(autor);
        UUID autorId = autorPersistido.getId();

        entityManager.clear();

        Autor autorRecarregado = entityManager.find(Autor.class, autorId);
        assertThat(autorRecarregado).isNotNull();

        entityManager.detach(autorRecarregado);

        List<Livro> livros = autorRecarregado.getLivros();

        assertThatThrownBy(livros::size).isInstanceOf(LazyInitializationException.class);
    }

}
