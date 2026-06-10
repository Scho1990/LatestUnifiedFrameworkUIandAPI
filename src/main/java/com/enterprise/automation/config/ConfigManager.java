package com.enterprise.automation.config;

import com.enterprise.automation.exceptions.FrameworkException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/config.properties")) {
            if (inputStream == null) {
                throw new FrameworkException("config/config.properties was not found on the classpath");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException exception) {
            throw new FrameworkException("Unable to load framework configuration", exception);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    public static String getRequired(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new FrameworkException("Missing required configuration value: " + key);
        }
        return value;
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {
        try {
            return Integer.parseInt(getRequired(key));
        } catch (NumberFormatException exception) {
            throw new FrameworkException("Configuration value must be an integer: " + key, exception);
        }
    }
}
