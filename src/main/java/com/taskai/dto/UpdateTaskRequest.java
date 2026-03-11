package com.taskai.dto;

import com.taskai.entity.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private TaskStatus status;
}
