package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.enterprise.context.ApplicationScoped;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ExampleResourceTest {
    static final String BASE_URL = "http://localhost:8080/message-board";
    @Test
    @Order(0)
    @DisplayName("Insert a new message with client should succeed")
    public void testPostHelloEndpoint() {
        given()
                .when()
                .cookie("clientId", "client0")
                .body("message")
                .post("/message-board")
                .then()
                .statusCode(201);
    }
    @Nested
    @QuarkusTest
    @ApplicationScoped
    class testEmptyUpdate {
        @Test
        @Order(0)
        @DisplayName("Insert a new message with client should succeed")
        public void testPostHelloEndpoint() {
            given()
                .when()
                    .cookie("clientId", "client0")
                    .body("message")
                    .post("/message-board")
                .then()
                    .statusCode(201);
        }

        @Test
        @Order(1)
        @DisplayName("Insert a new message with client should succeed")
        public void print() {
            given()
                .when()
                    .cookie("clientId", "client0")
                    .get("/message-board").body().prettyPrint();

        }
    }

    @Nested
    @QuarkusTest
    class testUpdateAfterInsert {
        @Test
        @Order(0)
        @DisplayName("Insert a new message with client should succeed")
        public void testInsert() {
            given()
                .when()
                    .cookie("clientId", "client0")
                    .body("message")
                    .post("/message-board")
                .then()
                    .statusCode(201);
        }

        @Test
        @Order(1)
        @DisplayName("Insert a new message with client should succeed")
        public void print() {
            given()
                    .when()
                    .cookie("clientId", "client0")
                    .get("/message-board").body().prettyPrint();

        }
    }

    @Nested
    @QuarkusTest
    class Flow1 {
        @Test
        @DisplayName("Insert a new message with client should succeed")
        public void testPostHelloEndpoint() {
            given()
                    .when()
                    .cookie("clientId", "client0")
                    .body("message")
                    .post("/message-board")
                    .then()
                    .statusCode(201);
        }

    }


}