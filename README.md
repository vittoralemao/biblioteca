# 📚 Biblioteca

Projeto de estudo e portfólio para praticar Spring Boot, Spring Data JPA, Spring Security e Spring Cloud, construído do zero em paralelo a um curso de Spring Boot.

## 🚀 Tecnologias

- Java 26
- Spring Boot 4.1.1
- Spring Data JPA / Hibernate
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

5. As requisições de exemplo estão disponíveis na collection do Postman, em `postman/collections/Biblioteca`.

## 📌 Status do projeto

Este projeto está em desenvolvimento ativo e sendo construído de forma incremental, conforme avanço nos estudos. No momento, o foco está na camada REST (Controllers, DTOs, tratamento de erros), com o CRUD de `Autor` completo.

### ✅ Já implementado
- Estrutura do projeto com Docker Compose (PostgreSQL + pgAdmin)
- Entidades `Livro` e `Autor` com relacionamento `@ManyToOne`/`@OneToMany`, IDs como `UUID`
- Auditoria automática de datas (`dataCadastro`/`dataAtualizacao`) via Spring Data JPA Auditing
- Repositórios com Spring Data JPA (Query Methods, `@Query`/JPQL, `@EntityGraph`)
- Testes de integração com `@DataJpaTest`
- **API REST de Autor — CRUD completo:**
  - `POST /autores` — cadastro
  - `GET /autores/{id}` — busca por id
  - `GET /autores` — listagem com filtro por `nome`/`nacionalidade`
  - `PUT /autores/{id}` — atualização
  - `DELETE /autores/{id}` — exclusão
- DTOs de request/response com Bean Validation (`@NotBlank`, `@NotNull`, `@Past`)
- Tratamento de erros centralizado (`@RestControllerAdvice` + `@ExceptionHandler`), com contrato de erro padronizado
- Collection do Postman versionada no repositório, cobrindo os 5 endpoints

### 🔜 Próximos passos
- Regras de negócio: bloqueio de duplicidade composta e de exclusão de Autor com Livro vinculado
- Swagger/OpenAPI
- Replicar a camada de API (DTO/Service/Controller) para `Livro`
- Paginação
- Spring Security
- Deploy (Spring Cloud)

## 📄 Licença

Este projeto está sob a licença MIT.
