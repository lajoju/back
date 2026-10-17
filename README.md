# Lajoju API

Backend com três serviços Spring Boot: `usuario`, `produto` e `agendamento`.
Usa PostgreSQL com schemas separados e autenticação JWT RS256. As chaves RSA
JWT estão definidas no código, em `Constants.java` de cada serviço.
As migrations Flyway inserem exemplos fictícios (nomes públicos de atores,
e-mails reservados e serviços) em qualquer ambiente. As contas de exemplo não
têm senha de login conhecida; use `/auth/register` para criar uma conta de teste.

## Executar

Requisitos: Docker Compose v2.

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
| PostgreSQL | `localhost:5434` | Banco com schemas `usuario`, `produto` e `agendamento` |

Cadastro e login (`POST /auth/register` e `POST /auth/login`) são públicos.
Os demais endpoints exigem o JWT no cabeçalho `Authorization`, usando o esquema
Bearer. O serviço `usuario` assina tokens com a chave privada; os serviços
validam com a chave pública. Como a chave privada está embutida no código, essa
configuração é adequada apenas para desenvolvimento e demonstração; não a use
em produção.

No serviço `usuario`, o perfil autenticado é consultado e atualizado por
`GET /usuarios/me` e `PUT /usuarios/me`. A atualização aceita nome, e-mail e
senha, mas não permite alterar o papel de funcionário. A consulta
`GET /usuarios/{id}/funcionario` retorna somente essa indicação e é usada pelo
serviço de agendamentos. Listagem, consulta geral, criação, atualização por ID
e exclusão de usuários não ficam disponíveis pela API enquanto não houver um
fluxo administrativo com autorização própria. Os funcionários de exemplo são
inseridos pela migration de desenvolvimento.

## Documentação e testes

- Swagger: `/swagger-ui/index.html` em cada serviço.
- Coleção Postman: [`postman/Lajoju.postman_collection.json`](./postman/Lajoju.postman_collection.json).
- Testes de um serviço: `.\mvnw.cmd clean test` dentro da pasta do serviço.
