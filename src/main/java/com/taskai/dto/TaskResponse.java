package com.taskai.dto;

import com.taskai.entity.Task;
import com.taskai.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private String aiResponse;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TaskResponse> subTasks;

    public static TaskResponse fromEntity(Task task) {
        if (task == null) return null;
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .aiResponse(task.getAiResponse())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .subTasks(task.getSubTasks() == null ? null : task.getSubTasks().stream().map(TaskResponse::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
