package com.github.vittoralemao.biblioteca.repository;

import com.github.vittoralemao.biblioteca.config.JpaAuditingConfig;
import com.github.vittoralemao.biblioteca.modules.autor.Autor;
import com.github.vittoralemao.biblioteca.modules.autor.AutorRepository;
import com.github.vittoralemao.biblioteca.modules.categoria.Categoria;
import com.github.vittoralemao.biblioteca.modules.livro.Livro;
import com.github.vittoralemao.biblioteca.modules.livro.LivroRepository;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.Nacionalidade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LivroRepositoryTest {

    private static final String TITULO_TESTE = "TITULO TESTE";
    private static final String AUTOR_TESTE = "AUTOR TESTE";
    private static final LocalDate DATA_NASCIMENTO_TESTE = LocalDate.of(1903, 6, 25);
    private static final String ISBN_TESTE = "978-85-508-0200-0";

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void deveSalvarEValidarDadosDoLivro(){
        Autor autor = new Autor();
        autor.setNome(AUTOR_TESTE);
        autor.setNacionalidade(criaNacionalidadeTest());
        autor.setDataNascimento(DATA_NASCIMENTO_TESTE);
        Autor autorSalvo = autorRepository.save(autor);

        Livro livro = new Livro();
        livro.setTitulo(TITULO_TESTE);
        livro.setAutor(autorSalvo);
        livro.setIsbn(ISBN_TESTE);
        livro.setCategoria(criaCategoriaTest());
        livro.setAnoPublicacao(2008);

        Livro livroSalvo = livroRepository.save(livro);
        Optional<Livro> livroRetornado = livroRepository.findById(livroSalvo.getId());

        assertThat(livroRetornado).isPresent().get().hasFieldOrPropertyWithValue("titulo", TITULO_TESTE);
    }

    @Test
    void deveSalvarEValidarLivroEAutorEmCascata(){
        Autor autorNaoSalvo = new Autor();
        autorNaoSalvo.setNome(AUTOR_TESTE);
        autorNaoSalvo.setNacionalidade(criaNacionalidadeTest());
        autorNaoSalvo.setDataNascimento(DATA_NASCIMENTO_TESTE);

        Livro livro = new Livro();
        livro.setTitulo(TITULO_TESTE);
        livro.setIsbn(ISBN_TESTE);
        livro.setCategoria(criaCategoriaTest());
        livro.setAnoPublicacao(2008);
        livro.setAutor(autorNaoSalvo);

        assertThatThrownBy(() -> livroRepository.save(livro))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }

    private Nacionalidade criaNacionalidadeTest(){
        Nacionalidade nacionalidade = new Nacionalidade();
        nacionalidade.setNome("NACIONALIDADE TESTE");
        nacionalidade.setIso("TST");
        return entityManager.persistAndFlush(nacionalidade);
    }

    private Categoria criaCategoriaTest(){
        Categoria categoria = new Categoria();
        categoria.setNome("CATEGORIA TESTE");
        return entityManager.persistAndFlush(categoria);
    }

}
