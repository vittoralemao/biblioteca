package com.github.vittoralemao.biblioteca.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenAPI(ObjectProvider<BuildProperties> buildProperties){
        String securitySchemeName = "bearerAuth";

        String versao = buildProperties.getIfAvailable() != null
                ? buildProperties.getIfAvailable().getVersion()
                : "dev";

        return new OpenAPI()
                .info(new Info()
                        .title("Biblioteca API")
                        .description("API REST para gerenciamento de um acervo de livros, autores, categorias e nacionalidades")
                        .version(versao))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public OperationCustomizer descricoesDePaginacao() {
        return (operation, handlerMethod) -> {
            if (operation.getParameters() == null) {
                return operation;
            }
            for (Parameter parametro : operation.getParameters()) {
                switch (parametro.getName()) {
                    case "page" -> {
                        parametro.setDescription("Número de página, começando em 0.");
                        parametro.getSchema().setMinimum(BigDecimal.ZERO);
                        parametro.getSchema().setDefault(null);
                        parametro.getSchema().setDefaultSetFlag(false);
                    }
                    case "size" -> {
                        parametro.setDescription("Quantidades de itens por página (de 1 a 100)");
                        parametro.getSchema().setMinimum(BigDecimal.ONE);
                        parametro.getSchema().setMaximum(BigDecimal.valueOf(100));
                        parametro.getSchema().setDefault(null);
                        parametro.getSchema().setDefaultSetFlag(false);
                    }
                    case "sort" ->
                        parametro.setDescription(
                            "Ordenação no formato campo,direção (asc ou desc). "
                            + "Exemplo: titulo,desc. Adicione mais linhas para ordenar por mais de um campo.");
                    default -> { }
                }
            }
            return operation;
        };
    }
}
