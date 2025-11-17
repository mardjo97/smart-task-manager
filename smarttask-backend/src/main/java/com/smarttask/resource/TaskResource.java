package com.smarttask.resource;

import com.smarttask.dto.TaskDTO;
import com.smarttask.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Task Management", description = "Operations for managing tasks")
public class TaskResource {

    @Inject
    TaskService taskService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Operation(summary = "Get all tasks", description = "Retrieve all tasks for the authenticated user")
    @APIResponse(responseCode = "200", description = "Tasks retrieved successfully")
    public Response getAllTasks(
            @QueryParam("completed") @Parameter(description = "Filter by completion status") Boolean completed,
            @QueryParam("category") @Parameter(description = "Filter by category") String category) {
        
        String userId = jwt.getClaim("userId");
        List<TaskDTO> tasks;

        if (completed != null) {
            tasks = taskService.getTasksByUserIdAndCompleted(userId, completed);
        } else if (category != null && !category.isEmpty()) {
            tasks = taskService.getTasksByUserIdAndCategory(userId, category);
        } else {
            tasks = taskService.getTasksByUserId(userId);
        }

        return Response.ok(tasks).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID")
    @APIResponse(responseCode = "200", description = "Task retrieved successfully")
    @APIResponse(responseCode = "404", description = "Task not found")
    public Response getTaskById(@PathParam("id") String id) {
        String userId = jwt.getClaim("userId");
        TaskDTO task = taskService.getTaskByIdAndUserId(id, userId);
        return Response.ok(task).build();
    }

    @POST
    @Operation(summary = "Create new task", description = "Create a new task")
    @APIResponse(responseCode = "201", description = "Task created successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    public Response createTask(@Valid TaskDTO taskDTO) {
        String userId = jwt.getClaim("userId");
        taskDTO.userId = userId;
        TaskDTO created = taskService.createTask(taskDTO);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update task", description = "Update an existing task")
    @APIResponse(responseCode = "200", description = "Task updated successfully")
    @APIResponse(responseCode = "404", description = "Task not found")
    public Response updateTask(@PathParam("id") String id, @Valid TaskDTO taskDTO) {
        TaskDTO updated = taskService.updateTask(id, taskDTO);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete task", description = "Delete a task by ID")
    @APIResponse(responseCode = "204", description = "Task deleted successfully")
    @APIResponse(responseCode = "404", description = "Task not found")
    public Response deleteTask(@PathParam("id") String id) {
        taskService.deleteTask(id);
        return Response.noContent().build();
    }
}
