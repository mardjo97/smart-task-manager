package com.smarttask.service;

import com.smarttask.dto.TaskDTO;
import com.smarttask.model.Task;
import com.smarttask.repository.TaskRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class TaskServiceTest {

    @Inject
    TaskService taskService;

    @InjectMock
    TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        when(taskRepository.listAll()).thenReturn(createMockTasks());
    }

    @Test
    public void testGetAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    private List<Task> createMockTasks() {
        Task task1 = new Task("Task 1", "Description 1", LocalDate.now(), "user1");
        Task task2 = new Task("Task 2", "Description 2", LocalDate.now().plusDays(1), "user1");
        return Arrays.asList(task1, task2);
    }
}
