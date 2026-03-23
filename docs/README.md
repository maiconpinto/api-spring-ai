# API Spring AI - Task Decomposition

## Visão Geral do Projeto
O **API Spring AI** é um sistema de gerenciamento de tarefas inteligente. Ele permite aos usuários criarem tarefas, e para tarefas consideradas complexas, o sistema utiliza Inteligência Artificial para decompor automaticamente a atividade principal em sub-tarefas menores e mais acionáveis.

A aplicação é construída utilizando Java e Spring Boot, contando com integração ao **Spring AI** para orquestrar as chamadas a modelos de linguagem (LLMs) via OpenRouter, especificamente o modelo `google/gemini-2.0-flash-001`.

O sistema também possui um módulo completo de autenticação JWT, garantindo que os usuários tenham acesso apenas às suas próprias tarefas.

## Estrutura da Documentação
Para facilitar o entendimento e manutenção, a documentação foi dividida nos seguintes arquivos:
- [TECHNOLOGIES.md](./TECHNOLOGIES.md): Lista de tecnologias, frameworks e versões utilizadas.
- [ARCHITECTURE.md](./ARCHITECTURE.md): Descrição detalhada da arquitetura do sistema e fluxo de dados.
- [API.md](./API.md): Documentação completa dos endpoints RESTful disponíveis.
- [FUNCTIONS.md](./FUNCTIONS.md): Detalhamento das principais funções, entradas, saídas e regras de negócio.

## Requisitos de Ambiente
- **Java**: 21
- **Maven**: 3.8+ (ou utilize o wrapper caso disponível)
- **Docker e Docker Compose**: Para rodar o banco de dados PostgreSQL.
- **Variáveis de Ambiente Necessárias**:
  - `OPENROUTER_API_KEY`: Chave de API para acesso aos modelos de IA.
  - `JWT_SECRET`: Chave secreta para assinatura dos tokens JWT.
  - `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`: Credenciais do banco (padrão: taskdb, postgres, postgres).

## Como Executar

### Via Docker
O projeto conta com um `docker-compose.yml` para facilitar a execução completa (Aplicação + Banco de Dados).

```bash
# Iniciar a aplicação e o banco de dados
docker-compose up -d
```

### Ambiente de Desenvolvimento (Local)
1. Suba o banco de dados utilizando Docker:
   ```bash
   docker-compose up db -d
   ```
2. Configure as variáveis de ambiente no seu sistema ou IDE (ex: arquivo `.env`).
3. Execute o projeto via Maven:
   ```bash
   mvn spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`.
