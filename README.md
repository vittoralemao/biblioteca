# 📚 Biblioteca

API REST para gerenciamento de um acervo de livros, autores, categorias e nacionalidades. Projeto de estudo e portfólio construído do zero buscando conhecimento prático em Spring Boot, Spring Data JPA e Spring Security.

🌐 **API em produção:** https://biblioteca.vitoralemao.com.br

## 🔎 Teste a API

1. Acesse https://biblioteca.vitoralemao.com.br (abre a documentação Swagger)
2. Faça `POST /login` com o usuário de demonstração:
```
login: demo@vitoralemao.com.br
senha: demo
```
3. Copie o token e clique em **Authorize**
4. Consulte livros, autores, categorias e nacionalidades

> O usuário demo tem o papel `OPERADOR` (somente leitura). Operações de escrita (`POST`, `PUT`, `DELETE`) retornam `403 Forbidden`, demonstrando a autorização por papéis.

## 🚀 Tecnologias

Java 26 · Spring Boot 4.1.1 · Spring Data JPA / Hibernate · Spring Security (JWT) · Bean Validation · springdoc-openapi (Swagger) · PostgreSQL 18 · Docker / Docker Compose · Maven · Lombok · JUnit 5 / AssertJ · AWS (EC2, RDS) · Caddy

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

## ☁️ Deploy na AWS

```
Internet ──HTTPS──▶ Caddy ──▶ API (Docker) ──▶ PostgreSQL (Amazon RDS)
                    └──── EC2 · sa-east-1 ────┘
```

- **Amazon EC2** (t3.micro, Amazon Linux 2023) executando a API e o Caddy em containers Docker
- **Amazon RDS** (PostgreSQL 18) em rede privada, acessível apenas pela API
- **Caddy** como reverse proxy, com HTTPS automático via Let's Encrypt
- **Security Groups:** apenas as portas 80/443 públicas; o banco aceita conexões somente do grupo da API
- Imagem publicada no **Docker Hub**, executando como usuário não-root, e configuração via variáveis de ambiente

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
> Opcionais: `ADMIN_EMAIL` e `ADMIN_SENHA` (credenciais do admin inicial) e `JPA_SHOW_SQL=true` (exibe o SQL no console).

5. Rode a aplicação e acesse o Swagger em `http://localhost:8080/swagger-ui/index.html`.

## 🔐 Acesso inicial

Ao subir com o banco vazio, a aplicação cria automaticamente um usuário `GERENTE` para o primeiro acesso:

```
login: admin@admin.com
senha: admin
```

> ⚠️ Credenciais padrão apenas para uso local. Em produção, são definidas pelas variáveis `ADMIN_EMAIL` e `ADMIN_SENHA`.

Faça `POST /login` com essas credenciais, copie o token e use-o no botão **Authorize** do Swagger.

## 🔜 Próximos passos

- Migrations de banco com Flyway
- Testes unitários da camada Service (JUnit e Mockito)
- Endpoint para troca de senha
- Pipeline de CI/CD

## 📄 Licença

Este projeto está sob a licença MIT.