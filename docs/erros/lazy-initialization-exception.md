# LazyInitializationException

**Pacote:** `org.hibernate.LazyInitializationException`

## Quando acontece
Ao tentar acessar um relacionamento marcado como `FetchType.LAZY`
(ex: `autor.getLivros()`) depois que a sessão do Hibernate (Persistence
Context) já foi encerrada. O Hibernate cria um "proxy" que só carrega os
dados reais na hora do acesso, se não existe mais uma sessão ativa
conectada ao banco naquele momento, ele não tem como buscar esses dados.

## Como identificar
A mensagem de erro geralmente contém algo como: failed to lazily initialize a collection of role: ...

## Como resolver/prevenir
- Acessar o relacionamento **dentro** do escopo transacional (ex: dentro
  de um método `@Transactional`, ou enquanto a sessão ainda está aberta).
- Usar `JOIN FETCH` ou `@EntityGraph` na query, trazendo o dado
  relacionado já na consulta original, sem depender do proxy.

## Exemplo no projeto
`exception/LazyInitializationExceptionTest#deveLancarExcecaoAoAcessarListaLazyForaDaSessao`