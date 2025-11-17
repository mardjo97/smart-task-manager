package com.smarttask.repository;

import com.smarttask.model.Task;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class TaskRepository implements PanacheMongoRepository<Task> {

    public List<Task> findByUserId(String userId) {
        return list("userId", userId);
    }

    public List<Task> findByUserIdAndCompleted(String userId, boolean completed) {
        return list("userId = ?1 and completed = ?2", userId, completed);
    }

    public List<Task> findByUserIdAndCategory(String userId, String category) {
        return list("userId = ?1 and category = ?2", userId, category);
    }

    public Task findByIdAndUserId(ObjectId id, String userId) {
        return find("_id = ?1 and userId = ?2", id, userId).firstResult();
    }
}
