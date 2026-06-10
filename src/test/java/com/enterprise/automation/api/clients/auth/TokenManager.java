package com.enterprise.automation.api.clients.auth;

import com.enterprise.automation.config.ConfigManager;
import com.enterprise.automation.exceptions.FrameworkException;
import io.restassured.response.Response;

import java.time.Instant;

import static io.restassured.RestAssured.given;

public final class TokenManager {
    private static String token;
    private static Instant expiryTime = Instant.EPOCH;

    private TokenManager() {
    }

    public static synchronized String getToken() {
        if (token == null || Instant.now().isAfter(expiryTime)) {
            refreshToken();
        }
        return token;
    }

    private static void refreshToken() {
        String staticToken = ConfigManager.get("api.token");
        if (staticToken != null && !staticToken.isBlank()) {
            token = staticToken;
            expiryTime = Instant.now().plusSeconds(ConfigManager.getInt("api.token.ttl.seconds"));
            return;
        }

        Response response = given()
                .contentType("application/json")
                .body("{\"username\":\"" + ConfigManager.getRequired("api.auth.username")
                        + "\",\"password\":\"" + ConfigManager.getRequired("api.auth.password") + "\"}")
                .post(ConfigManager.getRequired("api.auth.endpoint"));

        if (response.statusCode() >= 400) {
            throw new FrameworkException("Token request failed with status: " + response.statusCode());
        }

        token = response.jsonPath().getString(ConfigManager.getRequired("api.auth.token.path"));
        expiryTime = Instant.now().plusSeconds(ConfigManager.getInt("api.token.ttl.seconds"));
    }
}
