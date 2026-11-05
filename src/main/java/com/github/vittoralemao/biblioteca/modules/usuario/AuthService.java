package com.github.vittoralemao.biblioteca.modules.usuario;
import com.github.vittoralemao.biblioteca.share.CredenciaisInvalidasException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService {

    private static final String SENHA_FICTICIA = "senha-ficticia-para-equalizar-tempo";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final String hashFicticio;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.hashFicticio = passwordEncoder.encode(SENHA_FICTICIA);
    }

    public LoginResponseDTO login(LoginRequestDTO request){
        Optional<Usuario> usuario = usuarioRepository.findByLogin(request.login());
        String hash = usuario.map(Usuario::getSenha).orElse(hashFicticio);

        boolean senhaConfere = passwordEncoder.matches(request.senha(), hash);
        if (usuario.isEmpty() || !senhaConfere) {
            throw new CredenciaisInvalidasException();
        }

        return new LoginResponseDTO(jwtService.gerarToken(usuario.get()));
    }

}
