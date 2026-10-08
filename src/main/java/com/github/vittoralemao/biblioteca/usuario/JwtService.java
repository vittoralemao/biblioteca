package com.github.vittoralemao.biblioteca.usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiracao-minutos}")
    private long expiracaoMinutos;

    public String gerarToken(Usuario usuario){
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expiracaoMinutos * 60 * 1000);

        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("papel", usuario.getPapel().name())
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getChave())
                .compact();
    }

    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
