package com.github.vittoralemao.biblioteca.modules.livro;

import com.github.vittoralemao.biblioteca.modules.autor.Autor;
import com.github.vittoralemao.biblioteca.modules.autor.AutorNaoEncontradoException;
import com.github.vittoralemao.biblioteca.modules.autor.AutorRepository;
import com.github.vittoralemao.biblioteca.modules.autor.AutorResponseDTO;
import com.github.vittoralemao.biblioteca.modules.categoria.Categoria;
import com.github.vittoralemao.biblioteca.modules.categoria.CategoriaNaoEncontradaException;
import com.github.vittoralemao.biblioteca.modules.categoria.CategoriaRepository;
import com.github.vittoralemao.biblioteca.modules.categoria.CategoriaResponseDTO;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.Nacionalidade;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.NacionalidadeResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @InjectMocks
    LivroService livroService;

    @Mock
    LivroRepository livroRepository;

    @Mock
    AutorRepository autorRepository;

    @Mock
    CategoriaRepository categoriaRepository;

    private UUID nacionalidadeId;
    private UUID autorId;
    private UUID categoriaId;
    private UUID livroId;

    private Nacionalidade nacionalidade;
    private Autor autor;
    private Categoria categoria;
    private Livro livro;

    private LocalDate dataNascimento;

    @Test
    void deveCadastrarLivroComIsbn() {
        LivroRequestDTO livroDTO = criaDto(livro.getIsbn());

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));
        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(livroRepository.existsByIsbn(livro.getIsbn()))
                .thenReturn(false);
        when(livroRepository.save(any(Livro.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LivroResponseDTO resposta = livroService.cadastrar(livroDTO);

        assertThat(resposta.titulo()).isEqualTo("TITULO");
        assertThat(resposta.autor().id()).isEqualTo(autor.getId());
        assertThat(resposta.categoria().id()).isEqualTo(categoria.getId());
        assertThat(resposta.isbn()).isEqualTo("9788535914849");
        assertThat(resposta.anoPublicacao()).isEqualTo(2008);

        verify(livroRepository, never()).existsByTituloAndAutor(any(), any());   // no teste COM ISBN
        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void deveCadastrarLivroSemIsbn() {
        LivroRequestDTO livroDTO = criaDto(null);

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));
        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(livroRepository.existsByTituloAndAutor(livro.getTitulo(), autor))
                .thenReturn(false);
        when(livroRepository.save(any(Livro.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LivroResponseDTO resposta = livroService.cadastrar(livroDTO);

        assertThat(resposta.titulo()).isEqualTo("TITULO");
        assertThat(resposta.autor().id()).isEqualTo(autor.getId());
        assertThat(resposta.categoria().id()).isEqualTo(categoria.getId());
        assertThat(resposta.isbn()).isEqualTo(null);
        assertThat(resposta.anoPublicacao()).isEqualTo(2008);

        verify(livroRepository, never()).existsByIsbn(any());
        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void naoDeveCadastrarLivroComIsbnDuplicado() {
        LivroRequestDTO livroDTO = criaDto(livro.getIsbn());

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));
        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(livroRepository.existsByIsbn(livro.getIsbn()))
                .thenReturn(true);

        assertThatThrownBy(() -> livroService.cadastrar(livroDTO))
                .isInstanceOf(LivroDuplicadoException.class);

        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void naoDeveCadastrarLivroSemIsbnComTituloEAutorDuplicado() {
        LivroRequestDTO livroDTO = criaDto(null);

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));
        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.of(categoria));

        when(livroRepository.existsByTituloAndAutor(livro.getTitulo(), autor))
                .thenReturn(true);

        assertThatThrownBy(() -> livroService.cadastrar(livroDTO))
                .isInstanceOf(LivroDuplicadoException.class);

        verify(livroRepository, never()).existsByIsbn(any());
        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void naoDeveCadastrarLivroComAutorInexistente() {
        LivroRequestDTO livroDTO = criaDto(livro.getIsbn());

        when(autorRepository.findById(autorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livroService.cadastrar(livroDTO))
                .isInstanceOf(AutorNaoEncontradoException.class);

        verifyNoInteractions(categoriaRepository, livroRepository);
    }

    @Test
    void naoDeveCadastrarLivroComCategoriaInexistente() {
        LivroRequestDTO livroDTO = new LivroRequestDTO(
                livro.getTitulo(),
                autorId,
                categoriaId,
                livro.getIsbn(),
                livro.getAnoPublicacao());

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));
        when(categoriaRepository.findById(categoriaId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> livroService.cadastrar(livroDTO))
                .isInstanceOf(CategoriaNaoEncontradaException.class);

        verifyNoInteractions(livroRepository);
    }

    @BeforeEach
    void getUp(){
        nacionalidadeId = UUID.randomUUID();
        nacionalidade = new Nacionalidade();
        nacionalidade.setId(nacionalidadeId);
        nacionalidade.setNome("BRASILEIRA");
        nacionalidade.setIso("BRA");

        autorId = UUID.randomUUID();
        dataNascimento = LocalDate.of(2004, 12, 4);
        autor = new Autor();
        autor.setId(autorId);
        autor.setNome("AUTOR");
        autor.setNacionalidade(nacionalidade);
        autor.setDataNascimento(dataNascimento);

        categoriaId = UUID.randomUUID();
        categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.setNome("CATEGORIA");

        livroId = UUID.randomUUID();
        livro = new Livro();
        livro.setId(livroId);
        livro.setTitulo("TITULO");
        livro.setAutor(autor);
        livro.setCategoria(categoria);
        livro.setIsbn("9788535914849");
        livro.setAnoPublicacao(2008);
    }

    private LivroRequestDTO criaDto(String isbn){
        return new LivroRequestDTO(
                livro.getTitulo(),
                autorId,
                categoriaId,
                isbn,
                livro.getAnoPublicacao()
        );
    }
}
