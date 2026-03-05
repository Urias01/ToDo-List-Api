package com.todo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

                User executor = new User("executor", "executor@mail.com");

                when(userQueryRepository.findById(executor.getId()))
                                .thenReturn(Optional.of(executor));

                Task parent = new Task("Title", "Description", executor, TaskStatus.PENDING);

                parent.addResponsible(executor, executor);

                UUID parentId = UUID.randomUUID();

                when(taskQueryRepository.findById(parentId))
                                .thenReturn(Optional.of(parent));

                SubTaskRequest request = new SubTaskRequest(
                                "Subtask",
                                "Description",
                                List.of(executor.getId()),
                                TaskStatus.PENDING);

                when(taskCommandRepository.update(parent))
                                .thenReturn(parent);

                addSubtask.execute(request, parentId, executor.getId());

                assertEquals(1, parent.getSubtasks().size());
                assertTrue(parent.getSubtasks().stream()
                                .anyMatch(sub -> sub.getTitle().equals("Subtask")));
                verify(taskCommandRepository).update(parent);
        }

}
