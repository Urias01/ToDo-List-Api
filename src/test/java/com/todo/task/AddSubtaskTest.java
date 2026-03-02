package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todo.domain.task.entities.Task;
import com.todo.domain.task.presentation.requests.SubTaskRequest;
import com.todo.domain.user.entities.User;
import com.todo.domain.task.enums.TaskStatus;
import com.todo.application.ports.task.ITaskCommandRepository;
import com.todo.application.ports.task.ITaskQueryRepository;
import com.todo.application.ports.user.IUserQueryRepository;
import com.todo.application.usecase.task.AddSubtask;

@ExtendWith(MockitoExtension.class)
public class AddSubtaskTest {

        @InjectMocks
        private AddSubtask addSubtask;

        @Mock
        private IUserQueryRepository userQueryRepository;
        @Mock
        private ITaskCommandRepository taskCommandRepository;
        @Mock
        private ITaskQueryRepository taskQueryRepository;

        @Test
        @DisplayName("Should be able to create a subtask")
        void shouldBeAbleToCreateASubtask() {

                User loggedUser = new User();
                loggedUser.setId("user-123");

                when(userQueryRepository.findById("user-123"))
                                .thenReturn(Optional.of(loggedUser));

                Task parent = new Task("Title", "Description", loggedUser, TaskStatus.PENDING);

                parent.addResponsible(loggedUser, loggedUser);

                when(taskQueryRepository.findById("parent-123"))
                                .thenReturn(Optional.of(parent));

                SubTaskRequest request = new SubTaskRequest(
                                "Subtask",
                                "Description",
                                List.of("user-123"),
                                "parent-123",
                                TaskStatus.PENDING);

                when(taskCommandRepository.update(parent))
                                .thenReturn(parent);

                addSubtask.execute(request, "user-123");

                assertEquals(1, parent.getSubtasks().size());
                assertTrue(parent.getSubtasks().stream()
                                .anyMatch(sub -> sub.getTitle().equals("Subtask")));
                verify(taskCommandRepository).update(parent);
        }

}
