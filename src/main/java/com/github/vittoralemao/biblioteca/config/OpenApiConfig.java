package com.github.vittoralemao.biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Biblioteca API")
                        .description("API REST para gerenciamento de um acervo de livros, autores, categorias e nacionalidades")
                        .version("1.0.0"));
    }
}
