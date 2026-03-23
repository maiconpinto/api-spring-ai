# Arquitetura do Sistema

A arquitetura do **API Spring AI** segue os padrões clássicos e robustos de aplicações corporativas Spring Boot, adotando um modelo em camadas (Layered Architecture) fortemente focado em REST APIs e serviços de domínio.

## Camadas da Aplicação

1. **Camada de Apresentação (Controllers)**:
   - Contém os Controladores REST (`AuthController`, `TaskController`) responsáveis por receber as requisições HTTP, delegar para os serviços e retornar as respostas formatadas em JSON.
   - O pacote `dto` atua aqui definindo os objetos de transferência de dados (ex: `AuthRequest`, `TaskResponse`).

2. **Camada de Serviço (Services)**:
   - Centraliza as regras de negócio.
   - `TaskService`: Lida com as operações de CRUD das tarefas e gerencia a relação das tarefas com os usuários.
   - `TaskDecompositionService`: Comunica-se com o provedor de IA via `ChatClient` (Spring AI) para a decomposição inteligente das tarefas.
   - `JwtService`: Responsável por gerar e extrair dados de tokens JWT.

3. **Camada de Persistência (Repositories & Entities)**:
   - `Entities`: Classes Java que mapeiam as tabelas do banco de dados utilizando anotações do JPA/Hibernate (`User`, `Task`).
   - `Repositories`: Interfaces estendendo `JpaRepository` para interagir com o banco de dados PostgreSQL sem a necessidade de escrever queries SQL manualmente.

4. **Camada de Segurança (Security)**:
   - Utiliza Spring Security interceptando requisições através do `JwtAuthenticationFilter`.
   - Verifica a validade do token JWT nos cabeçalhos (`Authorization: Bearer <token>`) e insere o contexto de autenticação no `SecurityContextHolder`.

5. **Infraestrutura e Configuração (Config)**:
   - `AiConfig`: Configuração do cliente de IA.
   - `SecurityConfig`: Configuração de rotas públicas/privadas e filtros de segurança.
   - `ApplicationConfig`: Beans de injeção de dependência para provedores de autenticação, codificador de senhas, etc.

## Fluxo de Execução: Decomposição de Tarefas

1. O usuário autenticado faz um `POST /api/tasks` com título e descrição.
2. O `TaskController` recebe o DTO e chama o `TaskService`.
3. O `TaskService` chama o `TaskDecompositionService.filterAndDecompose`.
4. O serviço de IA monta um prompt de sistema rigoroso solicitando um JSON, passa os dados da tarefa e faz a requisição via OpenRouter para o Gemini.
5. A resposta é parseada e, caso a IA julgue que a tarefa deve ser decomposta, sub-tarefas são criadas e associadas à tarefa principal na memória.
6. O `TaskService` salva a tarefa principal (que salva em cascata as sub-tarefas) via `TaskRepository`.
7. O DTO de resposta é retornado ao cliente, contendo a tarefa e suas respectivas sub-tarefas se geradas.
