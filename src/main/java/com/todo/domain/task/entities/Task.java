package com.todo.domain.task.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.todo.domain.common.Auditable;
import com.todo.domain.common.exceptions.NotFoundException;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.domain.task.exception.CannotAddSubtaskToCancelledTaskException;
import com.todo.domain.task.exception.CannotBeNullException;
import com.todo.domain.task.exception.MaxResponsiblesReachedException;
import com.todo.domain.task.exception.SubtaskAlreadyHasParentException;
import com.todo.domain.task.exception.SubtaskIsNotPresentInTaskException;
import com.todo.domain.task.exception.TaskAlreadyFinishedException;
import com.todo.domain.task.exception.UserNotAllowedToAssignResponsibleException;
import com.todo.domain.task.exception.UserNotAllowedToPerformActionInTask;
import com.todo.domain.user.entities.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor
public class Task extends Auditable {

  @Id
  private UUID id;

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
  private Set<User> responsibles = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  @JsonBackReference
  private Task parentTask;

  @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private Set<Task> subtasks = new HashSet<>();

  public Task(String title, String description, User creator, TaskStatus status) {
    this.id = UUID.randomUUID();
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
      throw new TaskAlreadyFinishedException();

    this.title = title;
  }

  public void updateDescription(String description) {
    if (this.status == TaskStatus.FINISHED)
      throw new TaskAlreadyFinishedException();

    this.description = description;
  }

  public void addSubtask(Task subtask) {

    if (this.parentTask != null) {
      throw new SubtaskAlreadyHasParentException();
    }

    if (subtask == null)
      throw new CannotBeNullException("Subtask");

    if (this.status == TaskStatus.CANCELLED)
      throw new CannotAddSubtaskToCancelledTaskException();

    if (subtask.parentTask != null)
      throw new SubtaskAlreadyHasParentException();

    if (this.status == TaskStatus.FINISHED) {
      this.status = TaskStatus.IN_PROGRESS;
    }

    this.subtasks.add(subtask);
    subtask.parentTask = this;
  }

  public void updateSubtask(UUID subtaskId, String title, String description, User executor) {
    Task subtask = this.subtasks.stream().filter(x -> Objects.equals(x.getId(), subtaskId)).findFirst()
        .orElseThrow(SubtaskIsNotPresentInTaskException::new);

    subtask.assertUserCanModify(executor);

    subtask.rename(title);
    subtask.updateDescription(description);
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

  public void addResponsible(User executor, User newResponsible) {

    if (!this.createdBy.equals(executor)) {
      throw new UserNotAllowedToAssignResponsibleException();
    }

    if (newResponsible == null) {
      return;
    }

    validateResponsibleLimit(this.responsibles);

    if (this.responsibles.contains(newResponsible)) {
      throw new IllegalStateException("This user is already assigned in this task");
    }

    if (this.parentTask != null) {
      this.parentTask.responsibles.stream().map(User::getId)
          .forEach(responsibleId -> {
            if (responsibleId.equals(newResponsible.getId())) {
              throw new IllegalStateException(
                  "Cannot assign " + newResponsible.getName()
                      + " because they are not responsible for the parent task");
            }
          });
    }

    this.responsibles.add(newResponsible);
  }

  public void removeResponsible(User executor, User responsible) {

    if (!this.createdBy.equals(executor)) {
      throw new UserNotAllowedToAssignResponsibleException();
    }

    if (responsible == null) {
      return;
    }

    if (this.responsibles.size() < 1) {
      throw new IllegalStateException(
          "It is not possible to remove the person responsible, as this task does not have a person responsible.");
    }

    if (!this.responsibles.contains(responsible)) {
      throw new IllegalStateException("User not assigned");
    }

    this.responsibles.remove(responsible);
    this.subtasks.stream().forEach(sub -> sub.responsibles.remove(responsible));
  }

  public void assignResponsibleToSubtask(
      Task subtask,
      User executor,
      User responsible) {
    if (!executor.equals(this.createdBy)) {
      if (!executor.equals(responsible))
        throw new UserNotAllowedToAssignResponsibleException();

      if (!this.responsibles.contains(executor))
        throw new IllegalStateException("Executor is not responsible of parent task");
    }

    validateResponsibleLimit(subtask.responsibles);

    if (!this.responsibles.contains(responsible)) {
      throw new IllegalStateException("Cannot self assign in this task");
    }

    if (subtask.responsibles.contains(responsible)) {
      throw new IllegalStateException("This user arealdy assigned in this task");
    }

    subtask.responsibles.add(responsible);
  }

  private void validateResponsibleLimit(Set<User> responsibles) {
    if (responsibles.size() >= 2) {
      throw new MaxResponsiblesReachedException();
    }
  }

  public void changeStatus(TaskStatus newStatus) {
    switch (newStatus) {
      case PENDING -> pending();
      case IN_PROGRESS -> inProgress();
      case CANCELLED -> cancelled();
      case FINISHED -> finish();
    }
  }

  public void changeSubtaskStatus(UUID subtaskId, TaskStatus status, User executor) {
    assertUserCanModify(executor);

    Task subtask = this.subtasks.stream()
        .filter(s -> s.getId().equals(subtaskId))
        .findFirst()
        .orElseThrow(() -> new NotFoundException("Subtask"));

    subtask.changeStatus(status);
  }

  public void pending() {
    if (this.status.equals(TaskStatus.FINISHED)) {
      throw new TaskAlreadyFinishedException();
    }

    boolean hasActiveSubtasks = this.subtasks.stream()
        .anyMatch(sub -> !sub.getStatus().equals(TaskStatus.PENDING)
            && !sub.getStatus().equals(TaskStatus.CANCELLED));

    if (hasActiveSubtasks) {
      throw new IllegalStateException("Cannot change status to pending with subtasks in progress or finished");
    }

    this.status = TaskStatus.PENDING;
  }

  public void inProgress() {
    if (!this.status.equals(TaskStatus.PENDING) && !this.status.equals(TaskStatus.CANCELLED)) {
      throw new IllegalStateException("Invalid transition");
    }

    this.status = TaskStatus.IN_PROGRESS;
  }

  public void cancelled() {
    if (this.status.equals(TaskStatus.FINISHED)) {
      throw new TaskAlreadyFinishedException();
    }

    this.status = TaskStatus.CANCELLED;
  }

  public void finish() {

    if (this.status == TaskStatus.FINISHED) {
      throw new TaskAlreadyFinishedException();
    }

    boolean hasPendingSubtasks = this.subtasks.stream()
        .anyMatch(subtask -> subtask.getStatus() != TaskStatus.FINISHED);

    if (hasPendingSubtasks) {
      throw new IllegalStateException("Cannot finish task with pending subtasks");
    }

    this.status = TaskStatus.FINISHED;
    this.finishDate = LocalDateTime.now();
  }

  public void assertUserCanModify(User creator) {
    boolean isCreator = this.getCreatedBy().equals(creator);
    boolean isResponsible = this.getResponsibles().contains(creator);

    if (!isCreator && !isResponsible) {
      throw new UserNotAllowedToPerformActionInTask();
    }

    return;
  }

  public void assertUserCanView(User creator) {
    assertUserCanModify(creator);
  }

}
