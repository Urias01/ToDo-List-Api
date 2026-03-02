package com.todo.domain.user.presentation.request;

public record UserRequest(String name, String email, String password, String confirmPassword) {

}
