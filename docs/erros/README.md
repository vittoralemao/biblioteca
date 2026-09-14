# Catálogo de Erros e Exceções

Referência rápida de exceções comuns encontradas no desenvolvimento
deste projeto. Cada uma tem um arquivo dedicado com a causa raiz,
como identificar e como resolver.

| Exceção | Camada | Resumo |
|---|---|---|
| [LazyInitializationException](./lazy-initialization-exception.md) | JPA/Hibernate | Acesso a relacionamento `LAZY` fora da sessão ativa. |