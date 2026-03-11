package com.taskai.service;

import com.taskai.dto.CreateTaskRequest;
import com.taskai.dto.TaskResponse;
import com.taskai.dto.UpdateTaskRequest;
import com.taskai.entity.Task;
import com.taskai.entity.User;
import com.taskai.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskDecompositionService decompositionService;

    @Transactional
    public TaskResponse createTaskWithDecomposition(CreateTaskRequest request, User user) {
        Task rootTask = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(user)
                .build();

        // Save initially to get ID if needed, though Cascade processes correctly without it usually
        // decompositionService will process and add subtasks
        Task decomposedTask = decompositionService.filterAndDecompose(rootTask);
        return TaskResponse.fromEntity(taskRepository.save(decomposedTask));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getUserRootTasks(User user) {
        return taskRepository.findByUserAndParentTaskIsNullOrderByCreatedAtDesc(user)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(UUID id, User user) {
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return TaskResponse.fromEntity(task);
    }

    @Transactional
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request, User user) {
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());

        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(UUID id, User user) {
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponse decomposeTask(UUID id, User user) {
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        // Clear existing subtasks
        if (task.getSubTasks() != null) {
            task.getSubTasks().clear();
        }

        Task decomposedTask = decompositionService.filterAndDecompose(task);
        return TaskResponse.fromEntity(taskRepository.save(decomposedTask));
    }
}
