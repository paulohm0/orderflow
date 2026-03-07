# OrderFlow-API

API RESTful desenvolvida para o gerenciamento de fluxo de pedidos e controle de usuários.
Este projeto atua como o núcleo lógico do sistema, responsável pela persistência de dados, segurança,
processamento assíncrono e notificação em tempo real.

A aplicação foi construída utilizando o ecossistema Java e Spring, focando em boas práticas, clean code
e padrões de arquitetura MVC e Repository Pattern.


## Estrutura do projeto

```
/src/main/java/paulodev/orderflowapi
    /application                 ### Camada de Aplicação
      /dto                       # Objetos de transferência de dados entre camadas
      /service                   # Serviços da Applicação
    /config                      ### Configurações em geral
      /listener                  # Consumidor assíncrono (rabbitMQ)
      /messaging                 # Configuração do rabbitMQ
      /secutiry                  # Configurações de Autenticação, CORS e Filtro JWT
    /domain                      ### Camada de Domínio
      /entity                    # Entidades principais
      /enums                     # Enums
      /repository                # Objetos de acesso a dados utilizando Repository
    /web                         ### Camada WEB
      /controller                # Controllers / Endpoints 
      /exception                 # Exceções / Global handler
    OrderflowApplication.java    # Entrypoint main
```

## Tecnologias

- **Java 21**
- **Spring Boot**
- **PostgreSQL**
- **RabbitMQ**
- **Maven**
- **Lombok**
- **Server-Sent Events (SSE)**
- **Spring Security & JWT**
- **Spring Data JPA e Hibernate**

## Práticas Adotadas
- **Clean Code**: Foco na legibilidade do código através de nomenclaturas explícitas, métodos com única responsabilidade e tratamento de erros.
- **MVC**: Separação clara entre as camadas e isolamento das regras de negócio.
- **Global Exception Handling**: Tratamento centralizado de erros.
- **DTO Pattern**: Transferência de dados entre camadas para isolamento da API.
- **Soft Delete**: Inativação de usuários em vez de exclusão física para preservar a integridade e o histórico do banco de dados.

## Funcionalidades Principais

### Gestão de Usuários
- Autenticação: Registro de contas e login com devolução de token JWT.
- Segurança: As senhas são criptografadas no banco de dados utilizando BCrypt.
- Controle de Exclusão: Exclusão física apenas para usuários sem histórico de pedidos. Usuários com pedidos atrelados recebem inativação lógica (Soft Delete), mantendo a integridade referencial do banco de dados.

### Gestão de Pedidos
- Criação e Enfileiramento: Pedidos nascem com status PENDING e o ID é enviado para uma fila no RabbitMQ.
- Processamento Assíncrono: Um Listener consome a fila, processa a regra de negócio e altera o status para COMPLETED.
- Notificação (SSE): Assim que o pedido é concluído pelo Listener, um evento é disparado via SSE para atualizar a interface do usuário em tempo real.
- Validação: Usuários possuem acesso restrito apenas aos seus próprios pedidos, bloqueando tentativas de visualização ou cancelamento de registros de terceiros.
- Cancelamento Lógico: Pedidos podem ser cancelados via método PATCH, desde que não tenham sido processados.
- Filtragem: A lista de pedidos possui parâmetros opcionais para retornar apenas registros específicos, filtrando pela descrição do pedido ou pelo seu status atual.

## API Endpoints
### Usuários

- Registrar um novo usuário
```http response
POST /auth/register
Content-Type: application/json

{
    "username": "paulodev",
    "email": "paulo@dev.com",
    "password": "123"
}


success-response: 201
{
  "message": "User created successfully",
  "username": "paulodev",
  "email": "paulo@dev.com"
}
```

- Autenticar Usuário (Login)
```http response
POST /auth/login
Content-Type: application/json

{
  "username": "paulodev",
  "password": "123"
}


success-response: 200
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "name": "paulodev"
}
```

- Listar Todos os Usuários
```http response
GET /auth/users-list
Authorization: Bearer {seu_token_jwt}


success-response: 200
[
  {
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "username": "paulodev",
    "email": "paulo@dev.com",
    "userStatus": "ACTIVE"
  }
]
```

- Atualizar dados do Usuário
```http response
PUT /auth/update-user
Authorization: Bearer {seu_token_jwt}
Content-Type: application/json

{
  "username": "paulodev_atualizado",
  "email": "paulo_novo@dev.com",
  "password": "nova_senha_321"
}


success-response: 200
{
  "message": "Updated informations",
  "username": "paulodev_atualizado",
  "email": "paulo_novo@dev.com"
}
```

- Deletar/Inativar Usuário
```http response
DELETE /auth/delete-user
Authorization: Bearer {seu_token_jwt}


success-response: 204
no response body
```
---
### Pedidos

- Criar um novo Pedido
```http response
POST /orders
Authorization: Bearer {seu_token_jwt}
Content-Type: application/json

{
  "description": "Notebook Dell Inspiron",
  "amount": 4500.00
}


success-response: 201
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "username": "paulodev",
  "description": "Notebook Dell Inspiron",
  "amount": 4500.00,
  "createdAt": "2026-02-26T14:30:00Z",
  "status": "PENDING"
}
```

- Listar Pedidos do Usuário
```http response
GET /orders/list?description=Notebook&status=PENDING
Authorization: Bearer {seu_token_jwt}


success-response: 200
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "username": "paulodev",
    "description": "Notebook Dell Inspiron",
    "amount": 4500.00,
    "createdAt": "2026-02-26T14:30:00Z",
    "status": "PENDING"
  }
]
```

- Cancelar Pedido
```http response
PATCH /orders/cancel/{orderId}
Authorization: Bearer {seu_token_jwt}


success-response: 204
no response body
```
---

## Tratamento de Erros
A API utiliza um interceptador global (@RestControllerAdvice) para capturar exceções de regras de negócio e retornar um JSON padronizado, evitando o vazamento de stack trace e facilitando o tratamento pelo Front-end.
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Usuário não localizado ou token expirado",
  "path": "/orders/list",
  "timestamp": "2026-02-26T15:30:00Z"
}
```

### Mapeamento de Status HTTP:

- 400 BAD REQUEST (InvalidOperationException): Acionado quando uma operação lógica é inválida, como tentar cancelar um pedido que já consta como concluído ou cancelado.

- 401 UNAUTHORIZED (AuthenticationException): Acionado em falhas de autenticação, como e-mail/senha incorretos no login ou uso de um token JWT inválido.

- 403 FORBIDDEN (ForbiddenOperationException): Acionado por bloqueios de segurança quando um usuário tenta deletar, alterar ou cancelar um recurso (pedido ou conta) que pertence a outro usuário.

- 404 NOT FOUND (ResourceNotFoundException): Acionado quando um ID de usuário ou pedido buscado não existe no banco de dados.

- 409 CONFLICT (ConflictException): Acionado em violações de integridade, como a tentativa de registrar uma nova conta com um e-mail já existente no sistema.
