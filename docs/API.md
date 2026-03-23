# Documentação da API

Todos os endpoints, exceto os de autenticação, requerem o envio do token JWT no cabeçalho:
`Authorization: Bearer <seu_token_jwt>`

---

## 1. Autenticação

### Registrar Usuário
Cria uma nova conta de usuário no sistema.

- **URL**: `/api/auth/register`
- **Método**: `POST`
- **Acesso**: Público

**Corpo da Requisição (JSON)**:
```json
{
  "name": "João da Silva",
  "email": "joao@email.com",
  "password": "senha_segura"
}
```

**Resposta de Sucesso (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5c..."
}
```

### Login
Autentica o usuário e retorna o token JWT.

- **URL**: `/api/auth/login`
- **Método**: `POST`
- **Acesso**: Público

**Corpo da Requisição (JSON)**:
```json
{
  "email": "joao@email.com",
  "password": "senha_segura"
}
```

**Resposta de Sucesso (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5c..."
}
```

---

## 2. Tarefas

### Criar Tarefa (com Decomposição Automática)
Cria uma tarefa e aciona a IA para decompô-la, se necessário.

- **URL**: `/api/tasks`
- **Método**: `POST`
- **Acesso**: Privado (Autenticado)

**Corpo da Requisição (JSON)**:
```json
{
  "title": "Criar um site de ecommerce",
  "description": "O site deve ter carrinho de compras e integração com pagamentos."
}
```

**Resposta de Sucesso (200 OK)**:
```json
{
  "id": "uuid-da-tarefa",
  "title": "Criar um site de ecommerce",
  "description": "O site deve ter carrinho...",
  "status": "DECOMPOSED",
  "subTasks": [
    {
      "id": "uuid-sub-1",
      "title": "Configurar repositório e infraestrutura",
      "status": "PENDING"
    },
    {
      "id": "uuid-sub-2",
      "title": "Implementar carrinho de compras",
      "status": "PENDING"
    }
  ]
}
```

### Listar Tarefas Principais
Retorna as tarefas de nível raiz do usuário autenticado.

- **URL**: `/api/tasks`
- **Método**: `GET`
- **Acesso**: Privado (Autenticado)

**Resposta de Sucesso (200 OK)**: Array de objetos TaskResponse.

### Buscar Tarefa por ID
Retorna os detalhes de uma tarefa específica (e suas sub-tarefas, se existirem).

- **URL**: `/api/tasks/{id}`
- **Método**: `GET`
- **Acesso**: Privado (Autenticado)

### Atualizar Tarefa
Atualiza dados básicos da tarefa (como título, descrição, status).

- **URL**: `/api/tasks/{id}`
- **Método**: `PUT`
- **Acesso**: Privado (Autenticado)

**Corpo da Requisição (JSON)**:
```json
{
  "title": "Novo Título",
  "description": "Nova descrição",
  "status": "IN_PROGRESS"
}
```

### Excluir Tarefa
Remove a tarefa do banco de dados (e as sub-tarefas vinculadas em cascata).

- **URL**: `/api/tasks/{id}`
- **Método**: `DELETE`
- **Acesso**: Privado (Autenticado)
- **Resposta**: `204 No Content`

### Forçar Decomposição de Tarefa
Se uma tarefa existente não foi decomposta, permite forçar o envio à IA.

- **URL**: `/api/tasks/{id}/decompose`
- **Método**: `POST`
- **Acesso**: Privado (Autenticado)
