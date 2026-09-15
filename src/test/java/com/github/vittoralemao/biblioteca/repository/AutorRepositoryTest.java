package com.github.vittoralemao.biblioteca.repository;

import com.github.vittoralemao.biblioteca.entity.Autor;
import com.github.vittoralemao.biblioteca.entity.Livro;
import com.github.vittoralemao.biblioteca.enums.Categoria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AutorRepositoryTest {

    private static final String NOME_TESTE = "George Orwell";
    private static final String NACIONALIDADE_TESTE = "Britânico";
    private static final String TITULO_TESTE = "1984";

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void deveBuscarAutorComLivrosUsandoEntityGraph(){
        Autor autor = new Autor();
        autor.setNome(NOME_TESTE);
        autor.setNacionalidade(NACIONALIDADE_TESTE);
        entityManager.persistAndFlush(autor);

        Livro livro = new Livro();
        livro.setTitulo(TITULO_TESTE);
        livro.setAutor(autor);
        livro.setCategoria(Categoria.HISTORIA);
        livro.setIsbn("978-85-508-0200-0");
        entityManager.persistAndFlush(livro);

        entityManager.clear();

        Optional<Autor> autorComLivro = autorRepository.findById(autor.getId());

        assertThat(autorComLivro).isPresent();
        assertThatCode(() -> autorComLivro.get().getLivros().size()).doesNotThrowAnyException();
    }

    @Test
    void deveSalvarEValidarDadosDoAutor() {
        Autor autor = new Autor();
        autor.setNome(NOME_TESTE);
        autor.setNacionalidade(NACIONALIDADE_TESTE);

        Autor autorSalvo = autorRepository.save(autor);
        Optional<Autor> autorEncontrado = autorRepository.findById(autorSalvo.getId());

        assertThat(autorEncontrado).isPresent().get().hasFieldOrPropertyWithValue("nome", NOME_TESTE);
    }

    @Test
    void deveBuscarAutorComLivrosSemLancarExcecaoLazy(){
        Autor autor = new Autor();
        autor.setNome(NOME_TESTE);
        autor.setNacionalidade(NACIONALIDADE_TESTE);
        Autor autorPersistido = entityManager.persistAndFlush(autor);

        Livro livro = new Livro();
        livro.setTitulo(TITULO_TESTE);
        livro.setCategoria(Categoria.FICCAO);
        livro.setAutor(autorPersistido);
        entityManager.persistAndFlush(livro);

        Long autorId = autorPersistido.getId();
        entityManager.clear();

        Optional<Autor> resultado = autorRepository.buscarAutorComLivrosPorId(autorId);
        assertThat(resultado).isPresent();

        Autor autorComLivro = resultado.get();
        entityManager.detach(autorComLivro);

        assertThatCode(() -> autorComLivro.getLivros().size()).doesNotThrowAnyException();
        assertThat(autorComLivro.getLivros()).hasSize(1);
    }

}
