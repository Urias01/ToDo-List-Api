package com.todo.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

                String loggedUserId = "creator-1";
                String responsibleId = "user-123";

                TaskRequest request = new TaskRequest(
                                "Test Task",
                                "Description",
                                List.of(responsibleId),
                                null);

                User creator = new User();
                creator.setId(loggedUserId);

                User responsible = new User();
                responsible.setId(responsibleId);

                when(userQueryRepository.findById(loggedUserId))
                                .thenReturn(Optional.of(creator));

                when(userQueryRepository.findById(responsibleId))
                                .thenReturn(Optional.of(responsible));

                when(taskCommandRepository.create(any(Task.class)))
                                .thenAnswer(invocation -> {
                                        Task task = invocation.getArgument(0);
                                        ReflectionTestUtils.setField(task, "id", "generated-task-id");
                                        return task;
                                });

                String taskId = createTask.execute(request, loggedUserId);

                assertEquals("generated-task-id", taskId);

                verify(taskCommandRepository).create(argThat(task -> task.getResponsibles().stream()
                                .anyMatch(u -> responsibleId.equals(u.getId()))));
        }
}