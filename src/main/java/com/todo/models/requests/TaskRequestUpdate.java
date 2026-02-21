package com.todo.models.requests;

import java.util.List;

public record TaskRequestUpdate(
    String title,
    String description,
    List<String> users,
    String status) {
}
