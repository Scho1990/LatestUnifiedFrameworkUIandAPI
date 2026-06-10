package com.enterprise.automation.utils;

import com.enterprise.automation.config.ConfigManager;
import com.enterprise.automation.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {
    private WaitUtils() {
    }

    public static WebElement waitForVisible(By locator) {
        return webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator) {
        return webDriverWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForUrlContains(String value) {
        return webDriverWait().until(ExpectedConditions.urlContains(value));
    }

    private static WebDriverWait webDriverWait() {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(ConfigManager.getInt("explicit.wait.seconds")));
    }
}
