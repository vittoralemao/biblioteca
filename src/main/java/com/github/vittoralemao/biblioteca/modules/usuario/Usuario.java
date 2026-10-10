package com.github.vittoralemao.biblioteca.modules.usuario;

import com.github.vittoralemao.biblioteca.share.EntidadeAuditavel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@NoArgsConstructor
public class Usuario extends EntidadeAuditavel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Papel papel;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + papel.name()));
    }

    @Override
    public String getPassword(){
        return senha;
    }

    @Override
    public String getUsername(){
        return login;
    }

    public Usuario(String nome, String login, String senha, Papel papel){
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.papel = papel;
    }
}
