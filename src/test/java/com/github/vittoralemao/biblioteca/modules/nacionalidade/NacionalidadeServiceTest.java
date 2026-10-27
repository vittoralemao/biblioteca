package com.github.vittoralemao.biblioteca.modules.nacionalidade;

import com.github.vittoralemao.biblioteca.modules.autor.AutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NacionalidadeServiceTest {

    @InjectMocks
    NacionalidadeService nacionalidadeService;

    @Mock
    NacionalidadeRepository nacionalidadeRepository;

    @Mock
    AutorRepository autorRepository;

    @Test
    void deveCadastrarNacionalidadeQuandoNomeEIsoNaoExiste() {
        NacionalidadeRequestDTO dto = new NacionalidadeRequestDTO("BRASILEIRA", "BRA");

        when(nacionalidadeRepository.existsByNome("BRASILEIRA")).thenReturn(false);
        when(nacionalidadeRepository.existsByIso("BRA")).thenReturn(false);
        when(nacionalidadeRepository.save(any(Nacionalidade.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NacionalidadeResponseDTO resposta = nacionalidadeService.cadastrar(dto);

        assertThat(resposta.nome()).isEqualTo("BRASILEIRA");
        assertThat(resposta.iso()).isEqualTo("BRA");
        verify(nacionalidadeRepository).save(any(Nacionalidade.class));
    }

    @Test
    void naoDeveCadastrarNacionalidadeComNomeExistente() {
        NacionalidadeRequestDTO dto = new NacionalidadeRequestDTO("BRASILEIRA", "BRA");

        when(nacionalidadeRepository.existsByNome("BRASILEIRA")).thenReturn(true);

        assertThatThrownBy(() -> nacionalidadeService.cadastrar(dto))
                .isInstanceOf(NacionalidadeDuplicadaException.class)
                .hasMessageContaining("nome");

        verify(nacionalidadeRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarNacionalidadeComIsoExistente() {
        NacionalidadeRequestDTO dto = new NacionalidadeRequestDTO("BRASILEIRA", "BRA");

        when(nacionalidadeRepository.existsByNome("BRASILEIRA")).thenReturn(false);
        when(nacionalidadeRepository.existsByIso("BRA")).thenReturn(true);

        assertThatThrownBy(() -> nacionalidadeService.cadastrar(dto))
                .isInstanceOf(NacionalidadeDuplicadaException.class)
                .hasMessageContaining("ISO");

        verify(nacionalidadeRepository, never()).save(any());
    }

    @Test
    void naoDeveAtualizarNacionalidadeComNomeDuplicado() {
        NacionalidadeRequestDTO dto = new NacionalidadeRequestDTO("BRASILEIRA", "BRA");
        UUID id = UUID.randomUUID();

        Nacionalidade nacionalidade = new Nacionalidade();
        nacionalidade.setId(id);
        nacionalidade.setNome("BRASILEIRA");
        nacionalidade.setIso("BRA");

        when(nacionalidadeRepository.findById(id)).thenReturn(Optional.of(nacionalidade));
        when(nacionalidadeRepository.existsByNomeAndIdNot("BRASILEIRA", id)).thenReturn(true);

        assertThatThrownBy(() -> nacionalidadeService.atualizar(id, dto))
                .isInstanceOf(NacionalidadeDuplicadaException.class)
                .hasMessageContaining("nome");

        verify(nacionalidadeRepository, never()).save(any());

    }

    @Test
    void naoDeveAtualizarNacionalidadeComIsoDuplicado() {
        NacionalidadeRequestDTO dto = new NacionalidadeRequestDTO("BRASILEIRA", "BRA");
        UUID id = UUID.randomUUID();

        Nacionalidade nacionalidade = new Nacionalidade();
        nacionalidade.setId(id);
        nacionalidade.setNome("BRASILEIRA");
        nacionalidade.setIso("BRA");

        when(nacionalidadeRepository.findById(id)).thenReturn(Optional.of(nacionalidade));
        when(nacionalidadeRepository.existsByNomeAndIdNot("BRASILEIRA", id)).thenReturn(false);
        when(nacionalidadeRepository.existsByIsoAndIdNot("BRA", id)).thenReturn(true);

        assertThatThrownBy(() -> nacionalidadeService.atualizar(id, dto))
                .isInstanceOf(NacionalidadeDuplicadaException.class)
                .hasMessageContaining("ISO");

        verify(nacionalidadeRepository, never()).save(any());

    }

    @Test
    void naoDeveExcluirNacionalidadeComAutorVinculado() {
        UUID id = UUID.randomUUID();

        Nacionalidade nacionalidade = new Nacionalidade();
        nacionalidade.setId(id);
        nacionalidade.setNome("BRASILEIRA");
        nacionalidade.setIso("BRA");

        when(nacionalidadeRepository.findById(id)).thenReturn(Optional.of(nacionalidade));
        when(autorRepository.existsByNacionalidade(nacionalidade)).thenReturn(true);

        assertThatThrownBy(() -> nacionalidadeService.excluir(id))
                .isInstanceOf(NacionalidadeComAutorVinculadoException.class);

        verify(nacionalidadeRepository, never()).delete(any(Nacionalidade.class));

    }

}
