# Lajoju API

Backend composto por três aplicações Spring Boot independentes: `usuario`,
`produto` e `agendamento`. A autenticação usa JWT RS256; o PostgreSQL armazena
os dados em schemas separados, um para cada serviço.

## Pré-requisitos

Para a execução recomendada com Docker Compose:

- Docker Desktop instalado e em execução (ou Docker Engine em Linux).
- Docker Compose v2 disponível como `docker compose`.
- JDK 25, necessário para gerar as chaves RSA com as APIs criptográficas nativas
  do Java.

Maven é necessário apenas para executar os serviços ou testes diretamente no
host. Os Dockerfiles usam Java 25 e Maven para compilar as imagens.

## Preparação inicial

Execute os passos abaixo na raiz do repositório.

1. Gere o par RSA, se os arquivos ainda não existirem:

   ```powershell
   java usuario/src/main/java/com/senai/lajoju/security/GeradorChavesJwt.java
   ```

   O comando cria `secrets/jwt-private.pem` e `secrets/jwt-public.pem`.
   A chave privada assina tokens e é montada somente no serviço `usuario`;
   os demais serviços recebem apenas a chave pública para validá-los. A pasta
   `secrets/` é ignorada pelo Git. Nunca compartilhe ou versione a chave
   privada.

2. Se ainda não houver um `.env` na raiz, crie-o a partir do exemplo:

   ```powershell
   Copy-Item .env.example .env
   ```

   Preserve o `.env` existente. Em ambos os casos, confira se `POSTGRES_DB`,
   `POSTGRES_USER` e `POSTGRES_PASSWORD` estão definidos e substitua a senha
   de exemplo por uma senha forte. O arquivo `.env` é ignorado pelo Git.

> Se os arquivos RSA já existirem, não execute o gerador novamente. Uma nova
> chave invalida os tokens emitidos com a chave anterior. Para uma rotação
> intencional, consulte a seção [Rotação das chaves JWT](#rotação-das-chaves-jwt).

## Iniciar com Docker Compose

Com Docker em execução, inicie os serviços a partir da raiz:

```powershell
docker compose up --build -d
```

O primeiro build pode levar alguns minutos. Confira o estado dos contêineres:

```powershell
docker compose ps
```

Consulte os logs, se necessário:

```powershell
docker compose logs -f
```

Para acompanhar apenas um serviço, por exemplo:

```powershell
docker compose logs -f usuario
```

Para parar os contêineres sem apagar os dados do PostgreSQL:

```powershell
docker compose down
```

Os dados permanecem no volume `postgres_data`. **Atenção:** o comando abaixo
remove esse volume e apaga os dados persistidos:

```powershell
docker compose down -v
```

## Serviços e endereços

| Serviço | Endereço local | Descrição |
| --- | --- | --- |
| `usuario` | `http://localhost:8081` | Cadastro, login, usuários e funcionários |
| `produto` | `http://localhost:8082` | Catálogo de serviços oferecidos |
| `agendamento` | `http://localhost:8083` | Criação e gerenciamento de agendamentos |
| PostgreSQL | `localhost:5433` | Banco compartilhado com schemas isolados |

Dentro da rede do Compose, os serviços se comunicam pelos nomes DNS `usuario`,
`produto` e `postgres`. O `agendamento` consulta `usuario` e `produto`
internamente na porta `8080`.

## Configuração do banco

O Compose inicia PostgreSQL 17 e cria um banco (`lajoju`) com três schemas:
`usuario`, `produto` e `agendamento`. O script
`infra/postgres/init-schemas.sql` cria os schemas na inicialização do banco.
Cada aplicação executa suas migrações Flyway no próprio schema.

As variáveis lidas do `.env` são:

| Variável | Uso |
| --- | --- |
| `POSTGRES_DB` | Nome do banco |
| `POSTGRES_USER` | Usuário do banco |
| `POSTGRES_PASSWORD` | Senha do banco; defina uma senha forte |

A porta `5433` é publicada no host e encaminhada para a porta interna `5432`.
Se as portas `5433` ou `8081`–`8083` já estiverem ocupadas, libere-as ou altere
as portas publicadas no `docker-compose.yml`.

O `.env.example` usa valores de desenvolvimento local; substitua a senha antes
de subir os serviços. Não reutilize esses valores em ambientes compartilhados
ou de produção.

## Autenticação JWT

O serviço `usuario` emite tokens assinados com **RS256** e validade de uma hora.
Os serviços `produto` e `agendamento` validam a assinatura com a chave pública.
O JWT contém o e-mail no `subject` e o UUID do usuário na claim `uid`.

1. Cadastre uma conta com `POST /auth/register`.
2. Faça login com `POST /auth/login`.
3. Envie o token recebido como `Authorization: Bearer <token>` aos endpoints
   protegidos.

Cadastro e login são públicos. Os endpoints de negócio também exigem JWT. A
documentação OpenAPI e a interface Swagger são públicas.

Senhas de usuário são armazenadas como hashes BCrypt; o BCrypt não é usado para
assinar JWTs.

### Rotação das chaves JWT

Rotacione as chaves somente quando necessário. Faça backup seguro do par
existente e então execute:

```powershell
java usuario/src/main/java/com/senai/lajoju/security/GeradorChavesJwt.java --force
docker compose up --build -d
```

A rotação invalida os tokens existentes; os usuários precisam autenticar-se
novamente. Proteja e armazene o backup da chave privada com segurança.

## Swagger / OpenAPI

| API | Swagger UI | Especificação JSON |
| --- | --- | --- |
| `usuario` | `http://localhost:8081/swagger-ui/index.html` | `http://localhost:8081/v3/api-docs` |
| `produto` | `http://localhost:8082/swagger-ui/index.html` | `http://localhost:8082/v3/api-docs` |
| `agendamento` | `http://localhost:8083/swagger-ui/index.html` | `http://localhost:8083/v3/api-docs` |

Na interface, use **Authorize** para informar o JWT e testar operações
protegidas.

## Coleção Postman

Importe [`postman/Lajoju.postman_collection.json`](./postman/Lajoju.postman_collection.json)
no Postman. Com o Compose em execução, envie as requisições na ordem:

1. **Autenticação → Cadastrar cliente**
2. **Autenticação → Login**
3. Crie o funcionário e o serviço
4. Crie o agendamento

A coleção salva automaticamente o token e os IDs retornados. Execute as
requisições de consulta/atualização depois que os respectivos IDs forem
preenchidos. A pasta **Limpeza (opcional)** exclui primeiro o agendamento e,
em seguida, o serviço e o funcionário.

## Endpoints

Todos os endpoints abaixo exigem JWT, exceto `POST /auth/register` e
`POST /auth/login`.

### Autenticação (`usuario`)

- `POST /auth/register`: cria cliente. Corpo:
  `{"name":"Nome","email":"nome@exemplo.com","password":"senha-com-pelo-menos-8-caracteres"}`
- `POST /auth/login`: recebe e-mail e senha; retorna `accessToken`,
  `tokenType` e `expiresAt`.

As senhas não são incluídas nas respostas e devem ter pelo menos 8 caracteres
e no máximo 72 bytes UTF-8.

### Usuários e funcionários (`usuario`)

- `GET /usuarios` e `GET /usuarios/{id}`
- `POST /usuarios`, `PUT /usuarios/{id}` e `DELETE /usuarios/{id}`

Criação e atualização recebem `name`, `email`, `password` e `flgFuncionario`.
O cadastro público sempre cria um cliente (`flgFuncionario: false`).

### Serviços/produtos (`produto`)

- `GET /produtos` e `GET /produtos/{id}`
- `POST /produtos`, `PUT /produtos/{id}` e `DELETE /produtos/{id}`

Corpo de exemplo:
`{"name":"Corte","tempoMedioMinutos":45,"price":35.00}`. O tempo é em minutos
e o preço não pode ser negativo.

### Agendamentos (`agendamento`)

- `GET /agendamentos` e `GET /agendamentos/{id}`
- `POST /agendamentos`, `PUT /agendamentos/{id}` e `DELETE /agendamentos/{id}`

Corpo para criar ou atualizar:
`{"usuarioId":"UUID_DO_CLIENTE","funcionarioId":"UUID_DO_FUNCIONARIO","produtoId":"UUID_DO_SERVICO","data":"2030-10-20","horaInicio":"09:00:00"}`.

O agendamento consulta o usuário, o funcionário e o serviço nos outros
serviços; exige que o cliente do pedido seja o usuário autenticado e que o
funcionário esteja marcado como funcionário. A hora final é calculada a partir
do tempo médio do serviço. Horários sobrepostos para o mesmo funcionário
retornam HTTP 409, e a duração deve terminar no mesmo dia.

## Execução local sem Docker (opcional)

Para executar as aplicações diretamente no host, ainda é necessário iniciar um
PostgreSQL acessível em `localhost:5433`, criar o banco e os três schemas e
gerar as chaves RSA. Inicie cada serviço a partir da respectiva pasta Maven:

```powershell
cd usuario
.\mvnw.cmd spring-boot:run
```

Repita em terminais separados para `produto` e `agendamento`. Por padrão, as
aplicações usam as URLs do PostgreSQL em `localhost:5433`, os schemas próprios,
e o cliente de agendamento aponta para `localhost:8081` e `localhost:8082`.
Esses valores podem ser substituídos com variáveis de ambiente, incluindo
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`,
`SPRING_DATASOURCE_PASSWORD`, `SECURITY_JWT_PUBLIC_KEY_LOCATION`,
`SECURITY_JWT_PRIVATE_KEY_LOCATION`, `USUARIO_SERVICE_URL` e
`PRODUTO_SERVICE_URL`.

Para executar os testes de um serviço:

```powershell
.\mvnw.cmd clean test
```

## Organização do código

Cada serviço separa responsabilidades em `model`, `controller`, `service`,
`dto`, `mapper`, `repository` e `security`. O PostgreSQL é atualizado pelas
migrações Flyway em `src/main/resources/db/migration`.
