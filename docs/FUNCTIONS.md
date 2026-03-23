# Resumo Técnico das Principais Funções

Este documento mapeia o propósito, fluxo e interface das principais funções de negócio que compõem o sistema.

---

## Serviço de Inteligência Artificial: `TaskDecompositionService`

### `filterAndDecompose(Task parentTask)`
Responsável pela inteligência central do sistema: analisar e quebrar tarefas.

- **Propósito**: Avalia se uma tarefa é simples ou complexa através de um prompt de sistema bem definido. Se for complexa, solicita à LLM que gere sub-tarefas em formato JSON.
- **Entrada**: 
  - `Task parentTask`: A entidade de tarefa recém-instanciada ou atualizada (contendo título e descrição).
- **Processamento**:
  1. Instancia um conversor (`BeanOutputConverter`) que extrai a estrutura de `TaskDecompositionResponse` e a converte em instruções de formato para a IA.
  2. Executa a chamada do `chatClient.prompt()`, concatenando as instruções de formato, o prompt de sistema rigoroso e os dados do usuário.
  3. Recebe a string de retorno da IA. Aplica um tratamento robusto contra formatações markdown indevidas (````json`).
  4. Realiza o parse via conversor para um objeto Java `TaskDecompositionResponse`.
  5. Se o `shouldDecompose` for verdadeiro, altera o status da `parentTask` para `DECOMPOSED` e itera sobre as sub-tarefas, associando-as à tarefa raiz.
- **Saída**: Retorna o próprio objeto `parentTask`, agora potencialmente populado com uma lista em `parentTask.getSubTasks()`.
- **Tratamento de Erros**: Ocorrendo falhas (ex: timeout da API ou JSON inválido), a exceção é pega, truncada e salva no campo `aiResponse` da tarefa para auditoria, sem interromper a criação da tarefa principal.

---

## Serviço de Tarefas: `TaskService`

### `createTaskWithDecomposition(CreateTaskRequest request, User user)`
- **Propósito**: Encapsula a lógica de negócio principal ao adicionar uma nova tarefa no banco de dados para um usuário.
- **Fluxo**: 
  1. Converte o DTO `CreateTaskRequest` em uma entidade `Task`.
  2. Associa a entidade ao `user` logado.
  3. Delega a entidade ao `TaskDecompositionService.filterAndDecompose`.
  4. Salva a entidade resultante (e possíveis sub-tarefas geradas) através do `taskRepository.save()`.
  5. Converte o retorno para `TaskResponse`.

### `getUserRootTasks(User user)`
- **Propósito**: Busca tarefas que pertencem a um usuário e que não são sub-tarefas (não possuem um `parentTask`).
- **Comportamento**: Utiliza o repositório JPA para filtrar e garantir que o usuário veja um quadro limpo (apenas os projetos/tarefas principais na visualização inicial).

---

## Serviço de Segurança: `JwtService`

### `generateToken(UserDetails userDetails)`
- **Propósito**: Emitir o passaporte digital de acesso à API (JWT).
- **Fluxo**: Usa o secret (`JWT_SECRET`) definido na configuração para assinar criptograficamente o e-mail (username) do usuário, configurando as datas de emissão e expiração do token.

### `isTokenValid(String token, UserDetails userDetails)`
- **Propósito**: Validar as requisições subsequentes.
- **Fluxo**: Compara o username contido dentro da claim do token com o username do `UserDetails` buscado do banco, além de checar o tempo de expiração do token (`isTokenExpired()`).
