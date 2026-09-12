package com.github.vittoralemao.biblioteca.repository;

import com.github.vittoralemao.biblioteca.entity.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AutorRepositoryTest {

    private static final String NOME_TESTE = "George Orwell";
    private static final String NACIONALIDADE_TESTE = "Britânico";

    @Autowired
    private AutorRepository autorRepository;

    @Test
    void deveSalvarEValidarDadosDoAutor() {
        Autor autor = new Autor();
        autor.setNome(NOME_TESTE);
        autor.setNacionalidade(NACIONALIDADE_TESTE);

        Autor autorSalvo = autorRepository.save(autor);
        Optional<Autor> autorEncontrado = autorRepository.findById(autorSalvo.getId());

        assertThat(autorEncontrado).isPresent().get().hasFieldOrPropertyWithValue("nome", NOME_TESTE);
    }

}
