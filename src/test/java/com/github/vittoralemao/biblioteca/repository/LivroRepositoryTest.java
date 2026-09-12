package com.github.vittoralemao.biblioteca.repository;
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
    private static final String ISBN_TESTE = "978-85-508-0200-0";

    @Autowired
    private LivroRepository livroRepository;

    @Test
    void deveSalvarEValidarDadosDoLivro(){
        Livro livro = new Livro();
        livro.setTitulo(TITULO_TESTE);
        livro.setAutor(AUTOR_TESTE);
        livro.setIsbn(ISBN_TESTE);
        livro.setCategoria(Categoria.TECNOLOGIA);
        livro.setAnoPublicacao(2008);

        Livro livroSalvo = livroRepository.save(livro);
        Optional<Livro> livroRetornado = livroRepository.findById(livroSalvo.getId());

        assertThat(livroRetornado).isPresent().get().hasFieldOrPropertyWithValue("titulo", TITULO_TESTE);
    }

}
