# 🆘 Help Me — Biblioteca

Guia de bolso para o dia a dia do projeto **Biblioteca API**.
Regras completas de versionamento: [docs/versionamento.md](docs/versionamento.md)

---

## 1. 🔁 Fluxo de trabalho (trunk-based)

A `main` está sempre pronta para produção. Todo trabalho entra por PR, com *squash merge*.

```bash
git switch main
git pull
git switch -c tipo/descricao-curta
# ... trabalho ...
git add caminho/dos/arquivos
git commit -m "tipo(escopo): descrição"
git push -u origin tipo/descricao-curta
# abrir PR no GitHub → revisar "Files changed" → squash merge
git switch main
git pull
git branch -d tipo/descricao-curta
```

| Prefixo da branch | Quando usar |
| :--- | :--- |
| `feat/` | Funcionalidade nova |
| `fix/` | Correção |
| `docs/` | Só documentação |
| `build/` | Build, dependências, versão do Java |

> 💡 **Um commit = uma mudança.** Use `git add` com os arquivos certos, não `git commit -am`.

---

## 2. 🏷️ Commits e versão (SemVer)

**Formato:** `tipo(escopo): descrição no presente, em português`

| Tipo | Quando usar | Efeito na versão |
| :--- | :--- | :--- |
| `feat!:` ou rodapé `BREAKING CHANGE:` | Quebra de contrato da API (campo, rota, formato) | MAJOR |
| `feat` | Funcionalidade nova compatível | MINOR |
| `fix` / `perf` | Correção ou melhoria de desempenho compatível | PATCH |
| `refactor` `test` `docs` `build` `chore` `style` | Sem efeito para o cliente | Não gera release sozinho |

- Entre releases, o `pom.xml` fica em `X.Y.Z-SNAPSHOT`.
- Uma release é uma tag Git anotada `vX.Y.Z`, igual à versão do `pom.xml`.
- Quebra de contrato futura ganha novo prefixo de rota (`/v2`), mantendo o `/v1` por um tempo.

---

## 3. 🧪 Build e testes

| Comando | O que faz |
| :--- | :--- |
| `mvnw.cmd clean verify` | Compila do zero e roda **todos** os testes (Testcontainers precisa do Docker aberto) |
| `mvnw.cmd test -Dtest=AuthServiceTest` | Roda **só uma** classe de teste |
| `docker build -t biblioteca:teste .` | Testa a imagem localmente (atenção: pula os testes) |
| `docker compose up -d` | Sobe o Postgres e o pgAdmin locais |
| `docker compose down` | Derruba os containers (os dados ficam no volume) |

Swagger local: `http://localhost:8080/swagger-ui/index.html`

---

## 4. ⚙️ Perfis e variáveis de ambiente

| Perfil | Como ativar | Para quê |
| :--- | :--- | :--- |
| `dev` | `SPRING_PROFILES_ACTIVE=dev` na Run Configuration | Desenvolvimento local, com valores padrão |
| `test` | Automático nos testes | Testes |
| *(nenhum)* | — | Produção: exige todas as variáveis abaixo |

| Variável | Obrigatória em produção? |
| :--- | :---: |
| `POSTGRES_USER`, `POSTGRES_PASSWORD` | ✅ |
| `JWT_SECRET` (mín. 32 caracteres) | ✅ |
| `ADMIN_EMAIL`, `ADMIN_SENHA` | ✅ |
| `POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_DB` | Têm valor padrão |
| `JPA_SHOW_SQL` | Opcional |

> 🔐 **Regra:** credencial nunca tem valor padrão no `application.yaml`. Valor padrão só no `application-dev.yml`.

---

## 5. 🗄️ Flyway

- Migration aplicada **nunca** é editada nem apagada. Correção vira migration nova.
- Nome: `V<n>__descricao_em_snake_case.sql`, com `n` sequencial.
- Arquivos em `src/main/resources/db/migration/`.

---

## 6. ☁️ Servidor (EC2)

| Situação | O que fazer |
| :--- | :--- |
| SSH dá **timeout** | Meu IP mudou: atualizar a regra da porta 22 no Security Group para **My IP** |
| Conferir as variáveis sem mostrar as senhas | `grep -c -E '^(POSTGRES_USER\|POSTGRES_PASSWORD)=' biblioteca.env` (esperado: 2) |
| Conferir dentro do container | `docker exec <container> printenv \| grep -c POSTGRES_` |

> ⚠️ Nunca usar `cat biblioteca.env`: o terminal guarda histórico.

---

## 7. 🩺 Diagnóstico rápido

| Sintoma | Causa provável |
| :--- | :--- |
| Erro de banco com `${ALGUMA_COISA}` no texto | Variável de ambiente não carregada |
| `Could not resolve placeholder` | Variável usada em `@Value` não definida |
| `release version X not supported` | JDK da máquina diferente do `java.version` do `pom.xml` |
| IntelliJ mostra vermelho, mas o `mvnw` passa | Recarregar o Maven; o terminal é a fonte da verdade |

---

## 8. 🌐 HTTP: status usados na API

| Status | Quando |
| :--- | :--- |
| `200 OK` | Leitura ou atualização bem-sucedida |
| `201 Created` | Recurso criado |
| `204 No Content` | Sucesso sem corpo (ex.: exclusão) |
| `400 Bad Request` | Dados inválidos (validação) |
| `401 Unauthorized` | Não autenticado: token ausente ou credenciais inválidas |
| `403 Forbidden` | Autenticado, mas sem permissão (ex.: OPERADOR em rota de GERENTE) |
| `404 Not Found` | Recurso não existe |
| `409 Conflict` | Duplicidade (ex.: ISBN já cadastrado) |
| `500 Internal Server Error` | Erro inesperado, sempre em Problem Details |

---

## 9. ↩️ Desfazer coisas no Git

| Comando | O que faz | Cuidado |
| :--- | :--- | :--- |
| `git restore Arquivo.java` | Descarta mudanças não preparadas | ⚠️ Perde as mudanças |
| `git restore --staged Arquivo.java` | Tira do `add`, mantém as mudanças | Seguro |
| `git commit --amend -m "msg"` | Corrige a mensagem do último commit | ⚠️ Só antes do `push` |
| `git reset HEAD~1` | Desfaz o último commit, mantendo as mudanças nos arquivos | ⚠️ Só antes do `push` |
| `git push --force-with-lease` | Envia histórico reescrito com segurança | ⚠️ Nunca na `main` compartilhada; nunca `--force` puro |