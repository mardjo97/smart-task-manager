package com.smarttask.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class AuthResourceTest {

    @Test
    public void testRegisterEndpoint() {
        String registerJson = String.format("""
                {
                    "username": "testuser_%d",
                    "email": "test_%d@example.com",
                    "password": "password123"
                }
                """, System.currentTimeMillis(), System.currentTimeMillis());

        given()
                .contentType(ContentType.JSON)
                .body(registerJson)
                .when().post("/api/auth/register")
                .then()
                .statusCode(201)
                .body("token", notNullValue())
                .body("user.username", notNullValue());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        String loginJson = """
                {
                    "username": "nonexistent",
                    "password": "wrongpassword"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(loginJson)
                .when().post("/api/auth/login")
                .then()
                .statusCode(401);
    }
}
