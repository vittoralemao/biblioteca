# 📚 Biblioteca

Projeto de estudo e portfólio para praticar Spring Boot, Spring Data JPA, Spring Security e Spring Cloud, construído do zero em paralelo a um curso de Spring Boot.

## 🌐 Demo ao vivo

A API está publicada e pode ser testada agora mesmo, sem precisar rodar nada localmente:

- **Aplicação**: https://biblioteca-9eu0.onrender.com
- **Documentação interativa (Swagger)**: https://biblioteca-9eu0.onrender.com/swagger-ui/index.html

> ⚠️ Hospedada no plano gratuito do [Render](https://render.com) — a instância "dorme" após um período sem uso, então a primeira requisição depois de um tempo parado pode demorar até ~50 segundos para responder (as seguintes voltam ao normal). O banco de dados roda no [Supabase](https://supabase.com).

Para testar as rotas protegidas, use as credenciais do usuário semeado (seção abaixo) para fazer login e obter um token.

## 🚀 Tecnologias

- Java 26
- Spring Boot 4.1.1
- Spring Data JPA / Hibernate
- Spring Web (REST)
- Spring Security (autenticação stateless com JWT)
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

5. Acesse a documentação interativa da API em `http://localhost:8080/swagger-ui/index.html`, onde é possível testar todos os endpoints diretamente pelo navegador — incluindo as rotas protegidas, usando o botão **Authorize** para informar o token JWT.

6. As requisições de exemplo também estão disponíveis na collection do Postman, em `postman/collections/Biblioteca`.

## 🔐 Acesso inicial (ambiente de desenvolvimento)

A API é protegida por autenticação JWT, e a maioria das rotas de escrita exige o papel `GERENTE`. Como não existe nenhum usuário por padrão em um banco novo, a aplicação sobe com um **semeador automático** (`CommandLineRunner`) que cria um usuário `GERENTE` inicial caso a tabela de usuários esteja vazia — resolvendo o problema de "ovo e galinha" de precisar de um gerente para criar o primeiro gerente.

Credenciais do usuário semeado:

```
login: admin@admin.com
senha: admin
```

> ⚠️ **Essas credenciais são apenas para desenvolvimento/demonstração local.** A senha é armazenada com hash (BCrypt) como qualquer outro usuário — não existe exceção de segurança para essa conta —, mas o valor em texto plano é intencionalmente simples e conhecido, então **não utilize este usuário (nem este padrão de seeder) em um ambiente de produção real** sem alterar a senha ou desativar o semeador.

Fluxo básico para testar a API autenticada:

1. `POST /login` com as credenciais acima → recebe o token JWT.
2. Use esse token no header `Authorization: Bearer <token>` (Postman) ou no botão **Authorize** do Swagger.
3. Com esse acesso, cadastre seu próprio usuário `GERENTE` via `POST /usuarios` e passe a usá-lo no lugar do admin semeado.

## 📌 Status do projeto

Este projeto está em desenvolvimento ativo e sendo construído de forma incremental, conforme avanço nos estudos. No momento, a camada completa de API REST está finalizada para as quatro entidades (Livro, Autor, Categoria e Nacionalidade), com autenticação/autorização via Spring Security, documentação via Swagger e regras de negócio aplicadas.

### ✅ Já implementado
- Estrutura do projeto com Docker Compose (PostgreSQL + pgAdmin)
- Entidades `Livro`, `Autor`, `Categoria` e `Nacionalidade`, com relacionamentos mapeados (`@ManyToOne`/`@OneToMany`), IDs como `UUID`
- Auditoria automática de datas e autor da última alteração (`dataCadastro`/`dataAtualizacao`/`usuarioUltimaAtualizacao`) via Spring Data JPA Auditing
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
- **Spring Security (autenticação e autorização):**
  - Cadastro de usuários (`login`, `senha`, `papel`) com senha criptografada via BCrypt
  - Autenticação stateless via JWT (geração, validação e extração de claims)
  - Filtro customizado (`OncePerRequestFilter`) para validar o token e popular o contexto de segurança em cada requisição
  - Autorização baseada em papéis com `@PreAuthorize`/`@EnableMethodSecurity`, restringindo rotas de escrita ao papel `GERENTE`
  - Tratamento customizado de `401 Unauthorized` e `403 Forbidden`, com o mesmo contrato de erro padronizado da API
  - Semeador automático do primeiro usuário `GERENTE`, resolvendo o bootstrap de autorização em um banco novo
  - Botão "Authorize" no Swagger (`@SecurityScheme`), permitindo testar rotas protegidas direto pela documentação interativa
- Documentação completa da API com Swagger/OpenAPI, incluindo exemplos de request/response e todos os códigos de status documentados (`200`, `201`, `204`, `400`, `401`, `403`, `404`, `409`)
- Collection do Postman versionada no repositório, cobrindo todos os endpoints

- **Deploy em produção** (Render + Supabase), com a mesma pipeline de autenticação/autorização validada em ambiente real

### 🔜 Próximos passos
- Testes unitários de camada Service

## 📄 Licença

Este projeto está sob a licença MIT.