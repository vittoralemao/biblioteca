package com.github.vittoralemao.biblioteca.config;

import com.github.vittoralemao.biblioteca.share.RespostaProblema;
import com.github.vittoralemao.biblioteca.share.TipoProblema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AutorizacaoNegadaHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        RespostaProblema.escrever(request, response, objectMapper,
                TipoProblema.ACESSO_NEGADO,
                "Você não tem permissão para executar essa ação.");
    }
}
