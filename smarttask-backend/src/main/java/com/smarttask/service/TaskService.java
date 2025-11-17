package com.smarttask.service;

import com.smarttask.dto.TaskDTO;
import com.smarttask.model.Task;
import com.smarttask.repository.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    public List<TaskDTO> getAllTasks() {
        return taskRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByUserIdAndCompleted(String userId, boolean completed) {
        return taskRepository.findByUserIdAndCompleted(userId, completed).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByUserIdAndCategory(String userId, String category) {
        return taskRepository.findByUserIdAndCategory(userId, category).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(String id) {
        Task task = taskRepository.findById(new ObjectId(id));
        if (task == null) {
            throw new NotFoundException("Task not found with id: " + id);
        }
        return toDTO(task);
    }

    public TaskDTO getTaskByIdAndUserId(String id, String userId) {
        Task task = taskRepository.findByIdAndUserId(new ObjectId(id), userId);
        if (task == null) {
            throw new NotFoundException("Task not found with id: " + id);
        }
        return toDTO(task);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.title = taskDTO.title;
        task.description = taskDTO.description;
        task.dueDate = taskDTO.dueDate;
        task.completed = taskDTO.completed;
        task.userId = taskDTO.userId;
        task.category = taskDTO.category;
        
        taskRepository.persist(task);
        return toDTO(task);
    }

    public TaskDTO updateTask(String id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(new ObjectId(id));
        if (task == null) {
            throw new NotFoundException("Task not found with id: " + id);
        }

        task.title = taskDTO.title;
        task.description = taskDTO.description;
        task.dueDate = taskDTO.dueDate;
        task.completed = taskDTO.completed;
        task.category = taskDTO.category;
        task.updateTimestamp();
        
        taskRepository.update(task);
        return toDTO(task);
    }

    public void deleteTask(String id) {
        Task task = taskRepository.findById(new ObjectId(id));
        if (task == null) {
            throw new NotFoundException("Task not found with id: " + id);
        }
        taskRepository.delete(task);
    }

    private TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.id != null ? task.id.toString() : null,
                task.title,
                task.description,
                task.dueDate,
                task.completed,
                task.userId,
                task.category
        );
    }
}
