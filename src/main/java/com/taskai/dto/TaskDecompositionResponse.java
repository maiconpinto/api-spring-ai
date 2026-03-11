package com.taskai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDecompositionResponse {
    private boolean shouldDecompose;
    private String reasoning;
    private List<SubTaskDto> subtasks;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubTaskDto {
        private String title;
        private String description;
    }
}
