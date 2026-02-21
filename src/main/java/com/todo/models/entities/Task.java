package com.todo.models.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.todo.models.common.Auditable;
import com.todo.models.enums.TaskStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor
public class Task extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String title;

  private String description;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;
  private LocalDateTime finishDate;

  @ManyToOne
  @JoinColumn(name = "created_by", referencedColumnName = "id")
  private User createdBy;

  @ManyToMany
  @JoinTable(name = "user_tasks", joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private Set<User> users = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  private Task parentTask;
  @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Task> subtasks = new HashSet<>();

  public Task(String title, String description, User creator, TaskStatus status) {
    if (creator == null)
      throw new IllegalArgumentException("Creator is required");

    if (title == null || title.isBlank())
      throw new IllegalArgumentException("Title is required");

    this.title = title;
    this.description = description;
    this.createdBy = creator;
    this.status = status != null ? status : TaskStatus.PENDING;
  }

  public void rename(String title) {
    if (title == null || title.isBlank())
      throw new IllegalArgumentException("Title is required");

    if (this.status == TaskStatus.FINISHED)
      throw new IllegalStateException("Cannot rename a finished task");

    this.title = title;
  }

  public void updateDescription(String description) {
    if (this.status == TaskStatus.FINISHED)
      throw new IllegalStateException("Cannot update description of a finished task");

    this.description = description;
  }

  public void addSubtask(Task subtask) {

    if (this.parentTask != null) {
      throw new IllegalStateException("Subtasks cannot have their own subtasks");
    }

    if (subtask == null)
      throw new IllegalArgumentException("Subtask cannot be null");

    if (subtask.parentTask != null)
      throw new IllegalStateException("Task already has a parent");

    this.subtasks.add(subtask);
    subtask.parentTask = this;
  }

  public void finishSubtask(Task subtask) {

    if (!this.subtasks.contains(subtask))
      throw new IllegalArgumentException("Subtask does not belong to this task");

    subtask.finish();

    boolean allFinished = this.subtasks.stream()
        .allMatch(t -> t.getStatus() == TaskStatus.FINISHED);

    if (allFinished) {
      this.finish();
    }
  }

  public void assignUsers(Set<User> users) {
    if (users == null || users.isEmpty()) {
      return;
    }
    this.users.addAll(users);
  }

  public void changeStatus(TaskStatus newStatus) {

    if (newStatus == null)
      throw new IllegalArgumentException("Status cannot be null");

    if (this.status == TaskStatus.FINISHED)
      throw new IllegalStateException("Cannot change a finished task");

    this.status = newStatus;
  }

  public void finish() {

    if (this.status == TaskStatus.FINISHED) {
      throw new IllegalStateException("Task already finished");
    }

    boolean hasPendingSubtasks = this.subtasks.stream()
        .anyMatch(subtask -> subtask.getStatus() != TaskStatus.FINISHED);

    if (hasPendingSubtasks) {
      throw new IllegalStateException("Cannot finish task with pending subtasks");
    }

    this.status = TaskStatus.FINISHED;
    this.finishDate = LocalDateTime.now();
  }

}
