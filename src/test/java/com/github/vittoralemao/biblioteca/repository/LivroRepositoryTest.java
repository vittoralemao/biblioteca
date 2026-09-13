package com.github.vittoralemao.biblioteca.repository;
import com.github.vittoralemao.biblioteca.entity.Autor;
import com.github.vittoralemao.biblioteca.entity.Livro;
import com.github.vittoralemao.biblioteca.enums.Categoria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LivroRepositoryTest {

    private static final String TITULO_TESTE = "Clean Code";
    private static final String AUTOR_TESTE = "Robert C. Martin";
    private static final String NACIONALIDADE_TESTE = "Americano";
    private static final String ISBN_TESTE = "978-85-508-0200-0";

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Test
    void deveSalvarEValidarDadosDoLivro(){
        Autor autor = new Autor();
        autor.setNome(AUTOR_TESTE);
        autor.setNacionalidade(NACIONALIDADE_TESTE);
        Autor autorSalvo = autorRepository.save(autor);

        Livro livro = new Livro();
        livro.setTitulo(TITULO_TESTE);
        livro.setAutor(autorSalvo);
        livro.setIsbn(ISBN_TESTE);
        livro.setCategoria(Categoria.TECNOLOGIA);
        livro.setAnoPublicacao(2008);

        Livro livroSalvo = livroRepository.save(livro);
        Optional<Livro> livroRetornado = livroRepository.findById(livroSalvo.getId());

        assertThat(livroRetornado).isPresent().get().hasFieldOrPropertyWithValue("titulo", TITULO_TESTE);
    }

    @Test
    void deveSalvarEValidarLivroEAutorEmCascata(){
        Livro livro = new Livro();
        livro.setTitulo(TITULO_TESTE);
        livro.setIsbn(ISBN_TESTE);
        livro.setCategoria(Categoria.TECNOLOGIA);
        livro.setAnoPublicacao(2008);

        Autor autor = new Autor();
        autor.setNome(AUTOR_TESTE);
        autor.setNacionalidade(NACIONALIDADE_TESTE);
        livro.setAutor(autor);
        livroRepository.save(livro);

        assertThat(autor.getId()).isNotNull();
        assertThat(livro.getId()).isNotNull();
    }
}
