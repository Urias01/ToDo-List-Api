package com.todo.infrastructure.persistence.jpa.task;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.todo.domain.task.entities.Task;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

  @Query("""
      SELECT t FROM Task t
      LEFT JOIN FETCH t.createdBy
      LEFT JOIN FETCH t.responsibles
      LEFT JOIN FETCH t.subtasks
      WHERE t.id = :id
      """)
  Optional<Task> findByIdWithDetails(UUID id);
}
