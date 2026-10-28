package com.github.vittoralemao.biblioteca.config;

import com.github.vittoralemao.biblioteca.share.RespostaProblema;
import com.github.vittoralemao.biblioteca.share.TipoProblema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AutenticacaoEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        RespostaProblema.escrever(request, response, objectMapper,
                TipoProblema.AUTENTICACAO_NECESSARIA,
                "Autenticação necessária. Informe um token válido.");
    }
}
