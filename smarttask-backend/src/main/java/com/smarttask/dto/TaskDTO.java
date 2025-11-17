package com.smarttask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TaskDTO {
    
    public String id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    public String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    public String description;
    
    public LocalDate dueDate;
    public boolean completed;
    public String userId;
    public String category;

    public TaskDTO() {}

    public TaskDTO(String id, String title, String description, LocalDate dueDate, boolean completed, String userId, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.userId = userId;
        this.category = category;
    }
}
