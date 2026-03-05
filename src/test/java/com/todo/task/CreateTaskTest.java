package com.todo.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.requests.TaskRequest;
import com.todo.domain.user.entities.User;
import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.application.usecase.task.CreateTask;

@ExtendWith(MockitoExtension.class)
public class CreateTaskTest {

        @InjectMocks
        private CreateTask createTask;

        @Mock
        private IUserQueryRepository userQueryRepository;

        @Mock
        private ITaskQueryRepository taskQueryRepository;

        @Mock
        private ITaskCommandRepository taskCommandRepository;

        @Test
        @DisplayName("Should create a task with responsible user successfully")
        void shouldCreateTaskSuccessfully() {

                User creator = new User("creator", "creator@mail.com");

                User responsible = new User("responsible", "responsible@mail.com");

                TaskRequest request = new TaskRequest(
                                "Test Task",
                                "Description",
                                List.of(responsible.getId()),
                                null);

                when(userQueryRepository.findById(creator.getId()))
                                .thenReturn(Optional.of(creator));

                when(userQueryRepository.findById(responsible.getId()))
                                .thenReturn(Optional.of(responsible));

                when(taskCommandRepository.create(any(Task.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                UUID taskId = createTask.execute(request, creator.getId());

                assertNotNull(taskId);

                verify(taskCommandRepository).create(argThat(task -> task.getResponsibles().stream()
                                .anyMatch(u -> responsible.getId().equals(u.getId()))));
        }
}