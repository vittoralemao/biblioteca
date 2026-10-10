package com.github.vittoralemao.biblioteca.modules.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO cadastrar (CadastroUsuarioRequestDTO request){
        String senhaCriptografada = passwordEncoder.encode(request.senha());

        Usuario usuario = new Usuario(request.nome(), request.login(), senhaCriptografada, request.papel());
        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getUsername(),  usuario.getPapel());
    }
}
