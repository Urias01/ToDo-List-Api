package com.todo.domain.user.entities;

import java.util.HashSet;
import java.util.Set;

import com.todo.domain.task.entities.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todo.domain.common.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String name;
  private String email;
  private String password;
  @ManyToMany(mappedBy = "responsibles")
  @JsonIgnore
  private Set<Task> tasks = new HashSet<>();
  @OneToMany(mappedBy = "createdBy")
  @JsonIgnore
  private Set<Task> createdTasks = new HashSet<>();

}
