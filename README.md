# Projeto Despesas

Sistema para controle de despesas pessoais. O usuário cria uma conta, faz login e cadastra seus gastos separados por categoria.

Fiz esse projeto para praticar autenticação com JWT no Spring Security, a construção de um front end em Angular e a integração entre os dois, tudo rodando em Docker.

## Tecnologias

- Java 17 e Spring Boot 4
- Spring Security + JWT
- Spring Data JPA com H2
- Angular 22 com Tailwind
- Docker, Docker Compose e Nginx

## Como rodar

Precisa ter o Docker instalado.

```bash
git clone https://github.com/EryckAvel/projeto-despesas.git
cd projeto-despesas
cp .env.example .env
```

Edite o .env e coloque uma chave no JWT_SECRET (dá pra gerar uma com openssl rand -base64 32). Depois:

```bash
docker compose up --build
```

A aplicação fica disponível em http://localhost.

## Endpoints
 
```
POST   /auth/cadastro
POST   /auth/login
PUT    /auth/alterar-senha
 
GET    /despesa
GET    /despesa/{id}
POST   /despesa
PUT    /despesa/{id}
DELETE /despesa/{id}
```

As rotas de `/despesa` precisam do token no header `Authorization: Bearer <token>`.

## O que ainda quero melhorar
 
- Fluxo de recuperação de senha por e-mail (hoje o alterar-senha é simples).
- Trocar o H2 por PostgreSQL.
- Implementar Testes.
- Implementar o cadastro de categorias, que hoje são fixas em um enum.


