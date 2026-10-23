package com.github.vittoralemao.biblioteca.modules.usuario;

import com.github.vittoralemao.biblioteca.share.CredenciaisInvalidasException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService authService;

    @Test
    void deveGerarTokenComCredenciaisValidas() {
        LoginRequestDTO dto = new LoginRequestDTO("demo@demo.com", "demo");
        Usuario usuario = new Usuario("usuario", "demo@demo.com", "hash-falso", Papel.OPERADOR);

        when(usuarioRepository.findByLogin(dto.login())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("demo", "hash-falso")).thenReturn(true);
        when(jwtService.gerarToken(usuario)).thenReturn("token-falso");

        LoginResponseDTO resposta = authService.login(dto);

        assertThat(resposta.token()).isEqualTo("token-falso");
    }

    @Test
    void naoDeveAutenticarComLoginInexistente() {
        LoginRequestDTO dto = new LoginRequestDTO("demo@demo.com", "demo");

        when(usuarioRepository.findByLogin(dto.login())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(CredenciaisInvalidasException.class);

        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void naoDeveAutenticarComSenhaErrada() {
        LoginRequestDTO dto = new LoginRequestDTO("demo@demo.com", "demo");
        Usuario usuario = new Usuario("usuario", "demo@demo.com", "hash-falso", Papel.OPERADOR);

        when(usuarioRepository.findByLogin(dto.login())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("demo", "hash-falso")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(CredenciaisInvalidasException.class);

        verifyNoInteractions(jwtService);
    }

}
