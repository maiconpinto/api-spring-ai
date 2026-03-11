package com.taskai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskai.dto.TaskDecompositionResponse;
import com.taskai.entity.Task;
import com.taskai.entity.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskDecompositionService {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            Você é um assistente especializado em gestão de projetos e produtividade.
            Seu objetivo é analisar uma tarefa fornecida pelo usuário e decidir se ela é complexa o suficiente para ser dividida em tarefas menores e mais acionáveis (sub-tarefas).
            Se a tarefa for simples e direta (ex: "comprar pão", "enviar email para João"), não deve ser decomposta.
            Se for complexa (ex: "criar um site de ecommerce", "organizar festa de casamento"), divida em sub-tarefas lógicas.
            
            Retorne EXCLUSIVAMENTE um JSON que obedeça a seguinte estrutura:
            {{
               "shouldDecompose": true | false,
               "reasoning": "Sua explicação rápida e objetiva do porquê desmembrou ou não",
               "subtasks": [
                   {{ "title": "título da sub-tarefa", "description": "descrição detalhada da sub-tarefa" }}
               ]
            }}
            Se shouldDecompose for false, o array subtasks deve ser vazio.
            Não retorne craseados de bloco de código markdown no início e no fim, APENAS O JSON.
            """;

    public Task filterAndDecompose(Task parentTask) {
        try {
            var converter = new BeanOutputConverter<>(TaskDecompositionResponse.class);
            String formatInstructions = converter.getFormat();

            String aiRawResponse = chatClient.prompt()
                    .system(s -> s.text(SYSTEM_PROMPT + "\n\nFormato obrigatório:\n{format}").param("format", formatInstructions))
                    .user("Analise e decomponha a seguinte tarefa: Titulo = '" + parentTask.getTitle() + "' | Descrição = '" + parentTask.getDescription() + "'")
                    .call()
                    .content();

            parentTask.setAiResponse(aiRawResponse);

            // Clean markdown json formatting if LLM includes it despite prompt
            if (aiRawResponse.startsWith("```json")) {
                aiRawResponse = aiRawResponse.substring(7);
            }
            if (aiRawResponse.startsWith("```")) {
                aiRawResponse = aiRawResponse.substring(3);
            }
            if (aiRawResponse.endsWith("```")) {
                aiRawResponse = aiRawResponse.substring(0, aiRawResponse.length() - 3);
            }
            
            aiRawResponse = aiRawResponse.trim();

            TaskDecompositionResponse parsedResponse = converter.convert(aiRawResponse);

            if (parsedResponse != null && parsedResponse.isShouldDecompose() && parsedResponse.getSubtasks() != null) {
                parentTask.setStatus(TaskStatus.DECOMPOSED);
                for (var subReq : parsedResponse.getSubtasks()) {
                    Task sub = Task.builder()
                            .title(subReq.getTitle())
                            .description(subReq.getDescription())
                            .status(TaskStatus.PENDING)
                            .user(parentTask.getUser())
                            .build();
                    parentTask.addSubTask(sub);
                }
            }
        } catch (Exception e) {
            log.error("Error during AI task decomposition", e);
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 500) {
                errorMsg = errorMsg.substring(0, 500) + "... [truncated]";
            }
            parentTask.setAiResponse("Erro ao processar: " + errorMsg);
        }
        return parentTask;
    }
}
