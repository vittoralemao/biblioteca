# 📚 Biblioteca

API REST para gerenciamento de um acervo de livros, autores, categorias e nacionalidades. Projeto de estudo e portfólio construído do zero buscando conhecimento prático em Spring Boot, Spring Data JPA e Spring Security.

## 🚀 Tecnologias

Java 26 · Spring Boot 4.1.1 · Spring Data JPA / Hibernate · Spring Security (JWT) · Bean Validation · springdoc-openapi (Swagger) · PostgreSQL 18 · Docker / Docker Compose · Maven · Lombok · JUnit 5 / AssertJ

## ✅ Funcionalidades

- **API REST completa (CRUD)** para Livro, Autor, Categoria e Nacionalidade, com filtros de busca
- **Regras de negócio:** bloqueio de duplicidade e de exclusão de registros com vínculos (`409 Conflict`)
- **Validação de dados** com Bean Validation e **tratamento de erros centralizado** com contrato padronizado
- **Persistência** com Spring Data JPA (Query Methods, JPQL, `JOIN FETCH`) e IDs `UUID`
- **Auditoria automática** de criação, alteração e usuário responsável
- **Segurança:** autenticação stateless com JWT, senhas com BCrypt e autorização por papéis (`GERENTE`/`OPERADOR`), com respostas `401`/`403` padronizadas
- **Documentação interativa** com Swagger/OpenAPI, incluindo autenticação pelo botão "Authorize"
- **Docker:** Docker Compose para o ambiente local e Dockerfile multi-stage
- **Testes de integração** da camada de persistência com `@DataJpaTest`

## ⚙️ Como rodar localmente

**Pré-requisitos:** Docker, Docker Compose e JDK 26.

1. Clone o repositório:
```bash
git clone https://github.com/vittoralemao/biblioteca.git
```

2. Crie um arquivo `.env` na raiz do projeto (usado pelo Docker Compose):
```
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha
POSTGRES_DB=nome_do_banco
PGADMIN_EMAIL=seu_email
PGADMIN_PASSWORD=sua_senha_pgadmin
```

3. Suba o banco de dados:
```bash
docker compose up -d
```

4. Defina as variáveis de ambiente da aplicação na sua IDE (o Spring Boot não lê o `.env`):
```
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha
POSTGRES_DB=nome_do_banco
JWT_SECRET=chave_secreta_com_pelo_menos_32_caracteres
```

5. Rode a aplicação e acesse o Swagger em `http://localhost:8080/swagger-ui/index.html`.

## 🔐 Acesso inicial

Ao subir com o banco vazio, a aplicação cria automaticamente um usuário `GERENTE` para o primeiro acesso:

```
login: admin@admin.com
senha: admin
```

> ⚠️ Credenciais apenas para uso local. Não utilize este usuário em produção.

Faça `POST /login` com essas credenciais, copie o token e use-o no botão **Authorize** do Swagger.

## 🔜 Próximos passos

- Deploy na AWS (API em container no EC2 + banco no RDS)
- Testes unitários da camada Service

## 📄 Licença

Este projeto está sob a licença MIT.
