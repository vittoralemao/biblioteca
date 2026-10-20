package com.github.vittoralemao.biblioteca.modules.categoria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

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
}
