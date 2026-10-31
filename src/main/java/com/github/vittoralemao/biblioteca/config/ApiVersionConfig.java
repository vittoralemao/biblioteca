package com.github.vittoralemao.biblioteca.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    public static final String PREFIXO_V1 = "/v1";
    private static final String PACOTE = "com.github.vittoralemao.biblioteca";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(PREFIXO_V1, HandlerTypePredicate.forBasePackage(PACOTE));
    }
}
