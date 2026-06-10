package com.enterprise.automation.api.clients;

import com.enterprise.automation.api.models.UserRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    public Response createUser(UserRequest request) {
        return given()
                .spec(RequestSpecFactory.authenticatedSpec())
                .body(request)
                .when()
                .post("/users");
    }

    public Response getUser(String userId) {
        return given()
                .spec(RequestSpecFactory.authenticatedSpec())
                .when()
                .get("/users/{userId}", userId);
    }
}
