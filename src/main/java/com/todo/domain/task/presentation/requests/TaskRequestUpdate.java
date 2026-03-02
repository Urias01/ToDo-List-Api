package com.todo.domain.task.presentation.requests;

import java.util.List;

public record TaskRequestUpdate(
        String title,
        String description,
        List<String> users,
        String status) {
}
