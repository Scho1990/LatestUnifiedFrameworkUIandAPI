package com.enterprise.automation.driver;

import com.enterprise.automation.exceptions.FrameworkException;
import org.openqa.selenium.WebDriver;

public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initializeDriver(String testMethodName) {
        if (DRIVER.get() == null) {
            DRIVER.set(DriverFactory.createDriver(testMethodName));
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new FrameworkException("WebDriver is not initialized for thread: " + Thread.currentThread().getName());
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
