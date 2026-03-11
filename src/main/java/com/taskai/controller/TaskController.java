package com.taskai.controller;

import com.taskai.dto.CreateTaskRequest;
import com.taskai.dto.TaskResponse;
import com.taskai.dto.UpdateTaskRequest;
import com.taskai.entity.User;
import com.taskai.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.createTaskWithDecomposition(request, user));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getRootTasks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getUserRootTasks(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTask(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/decompose")
    public ResponseEntity<TaskResponse> decomposeTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.decomposeTask(id, user));
    }
}
