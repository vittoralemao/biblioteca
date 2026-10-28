package com.github.vittoralemao.biblioteca.share;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RespostaProblema {

    private RespostaProblema() {
    }

    public static void escrever(HttpServletRequest request, HttpServletResponse response,
                                ObjectMapper objectMapper, TipoProblema tipo, String detalhe) throws IOException {

        ProblemDetail problema = tipo.criar(detalhe);

        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("type", String.valueOf(problema.getType()));
        corpo.put("title", problema.getTitle());
        corpo.put("status", problema.getStatus());
        corpo.put("detail", problema.getDetail());
        corpo.put("instance", request.getRequestURI());

        response.setStatus(problema.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(corpo));
    }
}
