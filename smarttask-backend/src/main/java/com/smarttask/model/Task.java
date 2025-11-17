package com.smarttask.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MongoEntity(collection = "tasks")
public class Task {
    
    public ObjectId id;
    public String title;
    public String description;
    public LocalDate dueDate;
    public boolean completed;
    public String userId;
    public String category;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Task() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.completed = false;
    }

    public Task(String title, String description, LocalDate dueDate, String userId) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.userId = userId;
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
