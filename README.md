# Lajoju API

Backend com três serviços Spring Boot: `usuario`, `produto` e `agendamento`.
Usa PostgreSQL com schemas separados e autenticação JWT RS256.
As migrations Flyway inserem exemplos fictícios (nomes públicos de atores,
e-mails reservados e serviços) em qualquer ambiente. As contas de exemplo não
têm senha de login conhecida; use `/auth/register` para criar uma conta de teste.

## Executar

Requisitos: Docker Compose v2 e JDK 25 (para gerar as chaves).

Na raiz do projeto, gere as chaves RSA uma vez:

```powershell
java usuario/src/main/java/com/senai/lajoju/security/GeradorChavesJwt.java
```

Crie o `.env` e ajuste a senha do banco:

```powershell
Copy-Item .env.example .env
```

Inicie os serviços:

```powershell
docker compose up --build -d
```

Para parar sem apagar os dados: `docker compose down`. `docker compose down -v`
também apaga o banco persistido.

## Serviços

| Serviço | URL | Função |
| --- | --- | --- |
| `usuario` | `http://localhost:8081` | Usuários, login e emissão de JWT |
| `produto` | `http://localhost:8082` | Serviços oferecidos |
| `agendamento` | `http://localhost:8083` | Agendamentos |
| PostgreSQL | `localhost:5433` | Banco com schemas `usuario`, `produto` e `agendamento` |

Cadastro e login (`POST /auth/register` e `POST /auth/login`) são públicos.
Os demais endpoints exigem o JWT no cabeçalho `Authorization`, usando o esquema
Bearer. O serviço `usuario` assina tokens com a chave privada; os serviços
validam com a chave pública.
Não versione nem compartilhe a pasta `secrets/` ou a chave privada.

## Documentação e testes

- Swagger: `/swagger-ui/index.html` em cada serviço.
- Coleção Postman: [`postman/Lajoju.postman_collection.json`](./postman/Lajoju.postman_collection.json).
- Testes de um serviço: `.\mvnw.cmd clean test` dentro da pasta do serviço.
