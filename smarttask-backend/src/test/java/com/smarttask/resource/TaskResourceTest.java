package com.smarttask.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class TaskResourceTest {

    @Test
    public void testTaskEndpointUnauthorized() {
        given()
                .when().get("/api/tasks")
                .then()
                .statusCode(401);
    }

    @Test
    public void testCreateTaskUnauthorized() {
        String taskJson = """
                {
                    "title": "Test Task",
                    "description": "Test Description",
                    "completed": false
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(taskJson)
                .when().post("/api/tasks")
                .then()
                .statusCode(401);
    }
}
