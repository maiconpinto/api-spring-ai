# Tecnologias e Frameworks

Este documento lista todas as tecnologias, linguagens, bibliotecas e frameworks utilizados no projeto, incluindo suas versões (conforme definido no `pom.xml`).

## Linguagem
- **Java**: 21

## Framework Principal
- **Spring Boot**: 3.4.3
  - **Spring Boot Starter Web**: Criação de APIs RESTful.
  - **Spring Boot Starter Data JPA**: Abstração de persistência e integração com o Hibernate.
  - **Spring Boot Starter Security**: Autenticação e autorização de usuários.
  - **Spring Boot Starter Validation**: Validação de dados de entrada via anotações (`@Valid`, `@NotNull`, etc).

## Inteligência Artificial
- **Spring AI**: 1.0.0-M6
  - **Spring AI OpenAI Starter**: Integração com a API compatível da OpenAI, configurada para apontar para o OpenRouter (modelo: `google/gemini-2.0-flash-001`).

## Banco de Dados e Migrações
- **PostgreSQL**: Driver JDBC (`org.postgresql:postgresql`) para o banco de dados relacional.
- **Flyway**: `flyway-core` e `flyway-database-postgresql` para controle de versão do esquema de banco de dados e migrações.

## Segurança
- **JSON Web Token (JWT)**: `io.jsonwebtoken` (jjwt) versão `0.12.5`
  - Utilizado para geração e validação de tokens de sessão.

## Ferramentas de Desenvolvimento
- **Lombok**: Redução de boilerplate de código Java (Getters, Setters, Builders, Constructors).
- **Spring Boot DevTools**: Recarregamento rápido em ambiente de desenvolvimento.
- **Docker & Docker Compose**: Contêinerização da aplicação e do banco de dados (arquivos `Dockerfile`, `Dockerfile.dev`, `docker-compose.yml`).

## Testes
- **Spring Boot Starter Test**: Bibliotecas de testes unitários e de integração (JUnit, Mockito, etc).
- **Spring Security Test**: Ferramentas para testes em um contexto de segurança.
