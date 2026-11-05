# Sessão do usuário nas Despesas

Este documento explica como a API identifica "quem é o usuário logado" em cada
requisição e como esse usuário é vinculado às despesas que ele cadastra,
lista, atualiza ou deleta.

## Visão geral

A API **não usa sessão HTTP tradicional** (sem `HttpSession`, sem cookie de
sessão no servidor). A autenticação é **stateless**, baseada em **JWT**: o
token enviado pelo cliente em cada requisição carrega a identidade do
usuário, e essa identidade é recolocada no contexto de segurança do Spring a
cada requisição, de forma isolada por thread.

```
Login (AuthController -> AuthService)
        |
        v
  JwtUtil.gerarToken()  -> token JWT com subject=email e claim idUsuario
        |
        v
  Cliente guarda o token e envia em "Authorization: Bearer <token>"
        |
        v
JwtAuthenticationFilter (roda em toda requisição, antes do controller)
        |
        v
  SecurityContextHolder.getContext().setAuthentication(...)
        |
        v
  UsuarioLogado.obterId()  (usado dentro do DespesaService)
        |
        v
  Despesa é filtrada/vinculada ao idUsuario do usuário logado
```

## Peças principais

### 1. `JwtUtil` — geração e leitura do token
`src/main/java/com/eryckavel/utils/JwtUtil.java`

- No login (`AuthService.login`), gera um token com:
  - `subject`: o e-mail do usuário (usado como username)
  - `claim "idUsuario"`: o ID do usuário no banco
  - `issuedAt` / `expiration`: controlados por `jwt.expiration`
  - assinado com uma chave HMAC (`jwt.secret`)
- Em cada requisição autenticada, o filtro usa `extrairUsername` e
  `extrairIdUsuario` para recuperar essas informações sem precisar consultar
  o banco pelo ID (o ID já viaja dentro do token).

### 2. `JwtAuthenticationFilter` — recria o "usuário logado" a cada request
`src/main/java/com/eryckavel/filter/JwtAuthenticationFilter.java`

Um `OncePerRequestFilter` registrado antes do
`UsernamePasswordAuthenticationFilter` (ver `SecurityConfig`). Para cada
requisição:

1. Lê o header `Authorization`; se não houver `Bearer <token>`, segue sem
   autenticar (rotas públicas, ex.: `/auth/**`).
2. Extrai o username (e-mail) do token.
3. Se ainda não há autenticação no contexto, carrega o `UserDetails` via
   `AuthService.loadUserByUsername`.
4. Valida o token (`jwtUtil.tokenValido`) comparando username e checando
   expiração.
5. Se válido, monta um `UsuarioAutenticado` (com o ID extraído do próprio
   token) e registra no `SecurityContextHolder` via
   `UsernamePasswordAuthenticationToken`.

Esse `SecurityContextHolder` é a "sessão" — só que ela vive apenas durante a
requisição atual (armazenada em uma `ThreadLocal`), e é reconstruída do zero
a cada chamada a partir do token. Não há estado guardado no servidor entre
requisições.

### 3. `UsuarioAutenticado` — o "principal" da autenticação
`src/main/java/com/eryckavel/security/UsuarioAutenticado.java`

Implementa `UserDetails` e carrega apenas `id`, `email` e `senha` (hash). É
esse objeto que fica como `principal` dentro do `Authentication` guardado no
contexto de segurança.

### 4. `UsuarioLogado` — ponto de acesso único ao usuário da requisição
`src/main/java/com/eryckavel/security/UsuarioLogado.java`

Classe utilitária estática que lê o `principal` atual do
`SecurityContextHolder` e expõe `obterId()`. É o único ponto que o resto da
aplicação usa para saber "quem está fazendo esta requisição", evitando passar
o usuário manualmente por parâmetro em todos os métodos.

### 5. `DespesaService` — uso prático da sessão
`src/main/java/com/eryckavel/service/DespesaService.java`

Todo método do serviço de despesas começa obtendo o usuário logado:

```java
Long idUsuario = UsuarioLogado.obterId();
```

E esse `idUsuario` é usado para:
- **listar**: `despesaRepository.findByUsuarioId(idUsuario)` — só retorna
  despesas do usuário autenticado.
- **cadastrar**: a despesa nova é vinculada (`despesaMapper.toEntity`) ao
  `Usuario` correspondente ao ID logado.
- **atualizar / deletar**: busca a despesa com
  `findByIdAndUsuarioId(id, idUsuario)` — garante que um usuário não possa
  alterar/apagar despesa de outro usuário, mesmo que adivinhe o ID da
  despesa.

### 6. `SecurityConfig` — stateless por design
`src/main/java/com/eryckavel/config/SecurityConfig.java`

```java
.sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

Essa linha é o que formaliza a decisão: o Spring Security nunca cria ou
consulta `HttpSession`. Toda a "sessão" é reconstruída a cada request a
partir do JWT.

## Por que essa abordagem

- **Sem estado no servidor**: facilita escalar a API horizontalmente (não
  precisa de sessão compartilhada/sticky session entre instâncias).
- **Isolamento de dados por usuário**: como todo acesso a despesas passa por
  `idUsuario` obtido do token, não é possível um usuário acessar despesas de
  outro, mesmo manipulando o ID na URL.
- **Simplicidade de uso**: qualquer método de serviço pode chamar
  `UsuarioLogado.obterId()` sem precisar receber o usuário como parâmetro
  vindo do controller.

## Fluxo resumido de uma requisição autenticada

1. Cliente faz `POST /auth/login` → recebe um JWT.
2. Cliente envia `Authorization: Bearer <token>` em requisições a
   `/despesas/**`.
3. `JwtAuthenticationFilter` valida o token e popula o
   `SecurityContextHolder` com um `UsuarioAutenticado`.
4. `DespesaController` → `DespesaService` chama `UsuarioLogado.obterId()`
   para saber de quem são as despesas sendo manipuladas.
5. Requisição termina, contexto de segurança é descartado (próxima
   requisição repete o processo a partir do token).