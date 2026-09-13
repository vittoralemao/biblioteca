# 📚 Biblioteca

Projeto de estudo e portfólio para praticar Spring Boot, Spring Data JPA, Spring Security e Spring Cloud, construído do zero em paralelo a um curso de Spring Boot.

## 🚀 Tecnologias

- Java 26
- Spring Boot 4.1.x
- Spring Data JPA / Hibernate
- PostgreSQL 18
- Docker / Docker Compose
- pgAdmin4
- Maven
- Lombok
- JUnit 5 / AssertJ

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
POSTGRES_USER=seu_usuario
POSTGRES_PASSWORD=sua_senha
POSTGRES_DB=nome_do_banco


3. Suba os containers do banco de dados:
```bash
   docker compose up -d
```

4. Rode a aplicação Spring Boot pela sua IDE ou via Maven.

## 📌 Status do projeto

Este projeto está em desenvolvimento ativo e sendo construído de forma incremental, conforme avanço nos estudos. No momento, o foco está na camada de persistência com Spring Data JPA.

### ✅ Já implementado
- Estrutura do projeto com Docker Compose (PostgreSQL + pgAdmin)
- Entidades `Livro` e `Autor` com relacionamento `@ManyToOne`/`@OneToMany`
- Configuração de cascade (`CascadeType.PERSIST`) no lado `Livro.autor`
- Repositórios com Spring Data JPA
- Testes de integração com `@DataJpaTest`

### 🔜 Próximos passos
- Query Methods (Spring Data JPA)
- Estratégias de fetch (JOIN FETCH / `@EntityGraph`)
- Camada REST (Controllers, DTOs)
- Spring Security
- Deploy (Spring Cloud)

## 📄 Licença

Este projeto está sob a licença MIT.