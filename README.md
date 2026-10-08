# 📚 Biblioteca

Projeto de estudo e portfólio para praticar Spring Boot, Spring Data JPA, Spring Security e Spring Cloud, construído do zero em paralelo a um curso de Spring Boot.

## 🚀 Tecnologias

- Java 26
- Spring Boot 4.1.1
- Spring Data JPA / Hibernate
- Spring Web (REST)
- springdoc-openapi (Swagger/OpenAPI 3.1)
- Bean Validation
- PostgreSQL 18
- Docker / Docker Compose
- pgAdmin4
- Maven
- Lombok
- JUnit 5 / AssertJ
- Postman (collection versionada no próprio repositório)

## ⚙️ Como rodar o projeto localmente

### Pré-requisitos
- Docker e Docker Compose instalados
- JDK 26 instalado
- Uma IDE de sua preferência (o projeto foi desenvolvido com IntelliJ)

### Passos

1. Clone o repositório:
```bash
   git clone https://github.com/vittoralemao/biblioteca.git
```

2. Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:
```
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha
POSTGRES_DB=nome_do_banco
```

3. Suba os containers do banco de dados:
```bash
   docker compose up -d
```

4. Rode a aplicação Spring Boot pela sua IDE ou via Maven.

5. Acesse a documentação interativa da API em `http://localhost:8080/swagger-ui/index.html`, onde é possível testar todos os endpoints diretamente pelo navegador.

6. As requisições de exemplo também estão disponíveis na collection do Postman, em `postman/collections/Biblioteca`.

## 📌 Status do projeto

Este projeto está em desenvolvimento ativo e sendo construído de forma incremental, conforme avanço nos estudos. No momento, a camada completa de API REST está finalizada para as quatro entidades (Livro, Autor, Categoria e Nacionalidade), com documentação via Swagger e regras de negócio aplicadas.

### ✅ Já implementado
- Estrutura do projeto com Docker Compose (PostgreSQL + pgAdmin)
- Entidades `Livro`, `Autor`, `Categoria` e `Nacionalidade`, com relacionamentos mapeados (`@ManyToOne`/`@OneToMany`), IDs como `UUID`
- Auditoria automática de datas (`dataCadastro`/`dataAtualizacao`) via Spring Data JPA Auditing
- Repositórios com Spring Data JPA (Query Methods, `@Query`/JPQL, consultas de existência, paginação)
- Testes de integração com `@DataJpaTest`
- **API REST completa (CRUD) para as 4 entidades:**
  - `Livro` — cadastro, busca por id, listagem paginada e filtrada, atualização, exclusão
  - `Autor` — cadastro, busca por id, listagem com filtros, atualização, exclusão
  - `Categoria` — cadastro, busca por id, listagem, atualização, exclusão
  - `Nacionalidade` — cadastro, busca por id, listagem, atualização, exclusão
- DTOs de request/response com Bean Validation (`@NotBlank`, `@NotNull`, `@Past`)
- Tratamento de erros centralizado (`@RestControllerAdvice` + `@ExceptionHandler`), com contrato de erro padronizado
- Regras de negócio de integridade referencial: bloqueio de exclusão de entidades com vínculos ativos (ex: não é possível excluir um Autor com Livros cadastrados, uma Categoria ou Nacionalidade em uso), retornando `409 Conflict`
- Bloqueio de duplicidade composta nas regras de cadastro/atualização
- Documentação completa da API com Swagger/OpenAPI, incluindo exemplos de request/response e todos os códigos de status documentados (`200`, `201`, `204`, `400`, `404`, `409`)
- Collection do Postman versionada no repositório, cobrindo todos os endpoints

### 🔜 Próximos passos
- Spring Security (autenticação e autorização)
- Testes unitários de camada Service
- Spring Cloud / Deploy

## 📄 Licença

Este projeto está sob a licença MIT.
