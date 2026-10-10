package com.github.vittoralemao.biblioteca.modules.usuario;

import com.github.vittoralemao.biblioteca.share.CredenciaisInvalidasException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request){
        Usuario usuario = usuarioRepository.findByLogin(request.login()).
                orElseThrow(CredenciaisInvalidasException::new);

        if(!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        String token = jwtService.gerarToken(usuario);
        return new LoginResponseDTO(token);
    }
}
