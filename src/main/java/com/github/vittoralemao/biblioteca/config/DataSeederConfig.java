package com.github.vittoralemao.biblioteca.config;

import com.github.vittoralemao.biblioteca.modules.usuario.Papel;
import com.github.vittoralemao.biblioteca.modules.usuario.Usuario;
import com.github.vittoralemao.biblioteca.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeederConfig {

    @Bean
    public CommandLineRunner seedGerenteInicial(UsuarioRepository usuarioRepository,
                                                PasswordEncoder passwordEncoder,
                                                @Value("${app.admin.email}") String email,
                                                @Value("${app.admin.senha}") String senha
                                                ) {

        return args -> {
            if (usuarioRepository.count() == 0) {
                String senhaCriptografada = passwordEncoder.encode(senha);
                Usuario gerente = new Usuario("admin",email, senhaCriptografada, Papel.GERENTE);
                usuarioRepository.save(gerente);
            }
        };
    }
}
