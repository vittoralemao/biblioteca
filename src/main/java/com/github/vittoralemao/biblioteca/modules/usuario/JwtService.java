package com.github.vittoralemao.biblioteca.modules.usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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

    public String extrairLogin(String token){
        return extrairClaims(token).getSubject();
    }

    public boolean tokenValido(String token){
        try {
            Claims claims = extrairClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException _) {
            return false;
        }
    }


    public Claims extrairClaims(String token){
        return Jwts.parser()
                .verifyWith(getChave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
