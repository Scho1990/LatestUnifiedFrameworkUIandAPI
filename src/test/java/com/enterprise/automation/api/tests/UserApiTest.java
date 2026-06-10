package com.enterprise.automation.api.tests;

import com.enterprise.automation.api.clients.UserClient;
import com.enterprise.automation.api.models.UserRequest;
import com.enterprise.automation.api.models.UserResponse;
import com.enterprise.automation.base.BaseApiTest;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Users API")
public class UserApiTest extends BaseApiTest {
    @Test(groups = {"api", "smoke"})
    public void shouldCreateUser() {
        UserRequest request = new UserRequest("automation-user", "qa-engineer");

        Response response = new UserClient().createUser(request);
        UserResponse userResponse = response.as(UserResponse.class);

        assertThat(response.statusCode()).isBetween(200, 201);
        assertThat(userResponse.getName()).isEqualTo(request.getName());
        assertThat(userResponse.getJob()).isEqualTo(request.getJob());
    }
}
