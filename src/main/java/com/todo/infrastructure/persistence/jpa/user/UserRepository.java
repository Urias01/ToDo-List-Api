package com.todo.infrastructure.persistence.jpa.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.todo.models.entities.User;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
  
  Optional<User> findByEmail(String email);
}
