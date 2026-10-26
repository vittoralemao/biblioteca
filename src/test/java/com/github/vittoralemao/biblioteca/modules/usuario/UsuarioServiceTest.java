package com.github.vittoralemao.biblioteca.modules.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UsuarioRepository usuarioRepository;

    @Test
    void deveCadastrarUsuarioComSenhaCriptografada() {
        CadastroUsuarioRequestDTO dto = new CadastroUsuarioRequestDTO(
                "demo",
                "demo@demo.com",
                "demo1234",
                Papel.OPERADOR);

        when(usuarioRepository.existsByLogin("demo@demo.com")).thenReturn(false);
        when(passwordEncoder.encode("demo1234")).thenReturn("hash-falso");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponseDTO resposta = usuarioService.cadastrar(dto);

        assertThat(resposta.nome()).isEqualTo("demo");
        assertThat(resposta.login()).isEqualTo("demo@demo.com");
        assertThat(resposta.papel()).isEqualTo(Papel.OPERADOR);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getSenha()).isEqualTo("hash-falso");
    }

    @Test
    void naoDeveCadastrarUsuarioComLoginDuplicado() {
        CadastroUsuarioRequestDTO dto = new CadastroUsuarioRequestDTO(
                "demo",
                "demo@demo.com",
                "demo1234",
                Papel.OPERADOR);

        when(usuarioRepository.existsByLogin("demo@demo.com")).thenReturn(true);
        assertThatThrownBy(() -> usuarioService.cadastrar(dto)).isInstanceOf(UsuarioDuplicadoException.class);

        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }


}
