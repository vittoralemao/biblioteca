package com.github.vittoralemao.biblioteca.modules.autor;

import com.github.vittoralemao.biblioteca.modules.livro.LivroRepository;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.Nacionalidade;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.NacionalidadeNaoEncontradaException;
import com.github.vittoralemao.biblioteca.modules.nacionalidade.NacionalidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {


    @InjectMocks
    AutorService autorService;

    @Mock
    AutorRepository autorRepository;

    @Mock
    NacionalidadeRepository nacionalidadeRepository;

    @Mock
    LivroRepository livroRepository;

    private UUID nacionalidadeId;
    private LocalDate dataNascimento;
    private Nacionalidade nacionalidade;

    @Test
    void deveCadastrarAutor() {
        AutorRequestDTO dto = new AutorRequestDTO("NOME",
                dataNascimento, nacionalidade.getId());

        when(nacionalidadeRepository.findById(nacionalidade.getId()))
                .thenReturn(Optional.of(nacionalidade));

        when(autorRepository.existsByNomeAndDataNascimentoAndNacionalidade("NOME",
                dataNascimento, nacionalidade))
                .thenReturn(false);

        when(autorRepository.save(any(Autor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AutorResponseDTO resposta = autorService.cadastrar(dto);

        assertThat(resposta.nome()).isEqualTo("NOME");
        assertThat(resposta.dataNascimento()).isEqualTo("2004-12-04");
        assertThat(resposta.nacionalidade().id()).isEqualTo(nacionalidade.getId());

        verify(autorRepository).save(any(Autor.class));

    }

    @Test
    void naoDeveCadastrarAutorDuplicado() {
        AutorRequestDTO dto = new AutorRequestDTO("NOME",
                dataNascimento, nacionalidade.getId());

        when(nacionalidadeRepository.findById(nacionalidade.getId()))
                .thenReturn(Optional.of(nacionalidade));

        when(autorRepository.existsByNomeAndDataNascimentoAndNacionalidade("NOME",
                dataNascimento, nacionalidade))
                .thenReturn(true);

        assertThatThrownBy(() -> autorService.cadastrar(dto))
                .isInstanceOf(AutorDuplicadoException.class);

        verify(autorRepository, never()).save(any());

    }

    @Test
    void naoDeveCadastrarAutorComNacionalidadeInexistente() {
        AutorRequestDTO dto = new AutorRequestDTO("NOME",
                dataNascimento, nacionalidadeId);

        when(nacionalidadeRepository.findById(nacionalidadeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autorService.cadastrar(dto))
                .isInstanceOf(NacionalidadeNaoEncontradaException.class);

        verifyNoInteractions(autorRepository);

    }

    @Test
    void naoDeveAtualizarAutorInexistente() {
        AutorRequestDTO dto = new AutorRequestDTO("NOME",
                dataNascimento, nacionalidadeId);
        UUID autorId = UUID.randomUUID();

        when(autorRepository.findById(autorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autorService.atualizar(autorId, dto))
                .isInstanceOf(AutorNaoEncontradoException.class);

        verify(autorRepository, never()).save(any());
        verifyNoInteractions(nacionalidadeRepository);
    }

    @Test
    void naoDeveExcluirAutorComLivroVinculado() {
        UUID autorId = UUID.randomUUID();
        Autor autor = new Autor();
        autor.setId(autorId);
        autor.setNome("NOME");
        autor.setDataNascimento(dataNascimento);
        autor.setNacionalidade(nacionalidade);

        when(autorRepository.findById(autorId)).thenReturn(Optional.of(autor));
        when(livroRepository.existsByAutor(autor)).thenReturn(true);

        assertThatThrownBy(() -> autorService.excluir(autorId))
                .isInstanceOf(AutorPossuiLivroVinculadoException.class);

        verify(autorRepository, never()).delete(any());
    }

    @BeforeEach
    void setUp() {
        nacionalidadeId = UUID.randomUUID();
        dataNascimento = LocalDate.of(2004, 12, 4);

        nacionalidade = new Nacionalidade();
        nacionalidade.setId(nacionalidadeId);
        nacionalidade.setNome("BRASILEIRA");
        nacionalidade.setIso("BRA");
    }
}
