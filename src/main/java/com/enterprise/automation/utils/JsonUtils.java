package com.enterprise.automation.utils;

import com.enterprise.automation.exceptions.FrameworkException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static String toJson(Object value) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new FrameworkException("Unable to serialize object to JSON", exception);
        }
    }

    public static <T> T fromJson(String json, Class<T> targetType) {
        try {
            return OBJECT_MAPPER.readValue(json, targetType);
        } catch (JsonProcessingException exception) {
            throw new FrameworkException("Unable to deserialize JSON to " + targetType.getSimpleName(), exception);
        }
    }
}
