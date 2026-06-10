package com.enterprise.automation.api.clients;

import com.enterprise.automation.api.clients.auth.TokenManager;
import com.enterprise.automation.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {
    private RequestSpecFactory() {
    }

    public static RequestSpecification defaultSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static RequestSpecification authenticatedSpec() {
        if (!ConfigManager.getBoolean("api.auth.enabled")) {
            return defaultSpec();
        }

        return new RequestSpecBuilder()
                .addRequestSpecification(defaultSpec())
                .addHeader("Authorization", "Bearer " + TokenManager.getToken())
                .build();
    }
}