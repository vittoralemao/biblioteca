# Política de versionamento

## 1. Versão da aplicação: SemVer (MAJOR.MINOR.PATCH)

| Mudança                                            | Commit                         | Versão  |
|----------------------------------------------------|--------------------------------|---------|
| Quebra de contrato da API (campo, rota, formato)   | `feat!:` ou `BREAKING CHANGE:` | MAJOR   |
| Funcionalidade nova compatível                     | `feat:`                        | MINOR   |
| Correção compatível                                | `fix:` / `perf:`               | PATCH   |
| Sem efeito para o cliente                          | `refactor:` `test:` `docs:` `build:` `chore:` `style:` | não gera release sozinho |

- Entre releases, o `pom.xml` fica em `X.Y.Z-SNAPSHOT` ("em desenvolvimento").
- Uma release é uma tag Git anotada `vX.Y.Z` cuja versão é igual à do `pom.xml`.

## 2. Commits: Conventional Commits

Formato: `tipo(escopo opcional): descrição no presente, em português`.
Quebra de contrato: `!` depois do tipo (`feat!: ...`) e um rodapé
`BREAKING CHANGE: o que quebrou e como migrar`.

## 3. Branches: trunk-based

- A `main` está sempre pronta para produção.
- Todo trabalho acontece em branch curta: `feat/...`, `fix/...`, `docs/...`.
- Entra na `main` por Pull Request, com o CI verde, usando *squash merge*.
  O título do PR segue o Conventional Commits.

## 4. Imagem Docker (nuvem)

- Publicada só pelo GitHub Actions, a partir de uma tag `vX.Y.Z`.
- Tags da imagem: `X.Y.Z` (imutável) e `X.Y` (aponta para o último patch).
- Produção roda sempre uma versão exata (`X.Y.Z`), nunca `latest`.
- Rollback = voltar para a versão `X.Y.Z` anterior.

## 5. Banco de dados: Flyway

- Migration aplicada nunca é editada nem apagada; correção vira migration nova.
- Nome: `V<n>__descricao_em_snake_case.sql`, com `n` sequencial.
- A numeração das migrations é independente da versão da aplicação.

## 6. Contrato da API

- Mudanças compatíveis (campo novo opcional, rota nova) não mudam a rota.
- Quebras de contrato futuras ganham um novo prefixo (`/v2`), mantendo o
  anterior por um período de transição.