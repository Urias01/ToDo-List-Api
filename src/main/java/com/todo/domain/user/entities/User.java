package com.todo.domain.user.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.todo.domain.task.entities.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.domain.common.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User extends Auditable {

  @Id
  private UUID id;
  private String name;
  private String email;
  private String password;
  @ManyToMany(mappedBy = "responsibles")
  @JsonIgnore
  private Set<Task> tasks = new HashSet<>();
  @OneToMany(mappedBy = "createdBy")
  @JsonIgnore
  private Set<Task> createdTasks = new HashSet<>();

  public User(String name, String email) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.email = email;
  }

  public void changeName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be empty");
    }

    this.name = name;
  }

  public void changeEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be empty");
    }

    this.email = email;
  }

  public void changePassword(String encodedPassword) {
    if (encodedPassword == null || encodedPassword.isBlank()) {
      throw new IllegalArgumentException("Password cannot be empty");
    }

    this.password = encodedPassword;
  }
}
