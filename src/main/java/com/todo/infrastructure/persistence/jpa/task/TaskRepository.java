package com.todo.infrastructure.persistence.jpa.task;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.todo.domain.task.entities.Task;

public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {

  @Query("""
      SELECT t FROM Task t
      LEFT JOIN FETCH t.users
      LEFT JOIN FETCH t.subtasks
      WHERE t.id = :id
      """)
  Optional<Task> findByIdWithDetails(String id);

}
