package com.github.vittoralemao.biblioteca.modules.categoria;

import com.github.vittoralemao.biblioteca.modules.livro.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaController categoriaController;

    @InjectMocks
    private CategoriaService categoriaService;


    @Test
    void deveCadastrarCategoriaQuandoNomeNaoExiste() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO("POESIA");
        when(categoriaRepository.existsByNome("POESIA")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO resposta = categoriaService.cadastrar(dto);

        assertThat(resposta.nome()).isEqualTo("POESIA");
        verify(categoriaRepository).save(any(Categoria.class));

    }

    @Test
    void naoDeveCadastrarCategoriaComNomeDuplicado(){
        CategoriaRequestDTO dto = new CategoriaRequestDTO("ROMANCE");
        when(categoriaRepository.existsByNome("ROMANCE")).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.cadastrar(dto))
                .isInstanceOf(CategoriaDuplicadaException.class);

        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void naoDeveAtualizarCategoriaComNomeDuplicado(){
        CategoriaRequestDTO dto = new CategoriaRequestDTO("FICCAO");
        UUID id = UUID.randomUUID();

        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome("FICCAO");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.existsByNomeAndIdNot("FICCAO", id)).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.atualizar(id, dto))
                .isInstanceOf(CategoriaDuplicadaException.class);

        verify(categoriaRepository, never()).save(any());

    }

    @Test
    void naoDeveAtualizarCategoriaComIdInexistente(){
        CategoriaRequestDTO dto = new CategoriaRequestDTO("FICCAO");
        UUID id = UUID.randomUUID();

        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.atualizar(id, dto))
                .isInstanceOf(CategoriaNaoEncontradaException.class);

        verify(categoriaRepository, never()).save(any());

    }

    @Test
    void naoDeveExcluirCategoriaComLivroVinculado(){
        UUID id = UUID.randomUUID();

        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome("ROMANCE");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));
        when(livroRepository.existsByCategoria(categoria)).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.excluir(id))
                .isInstanceOf(CategoriaComLivroVinculadoException.class);

        verify(categoriaRepository, never()).delete(any());

    }
}
