package com.enterprise.automation.base;

import com.enterprise.automation.driver.DriverManager;
import com.enterprise.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected void click(By locator) {
        WaitUtils.waitForClickable(locator).click();
    }

    protected void type(By locator, String value) {
        WaitUtils.waitForVisible(locator).clear();
        WaitUtils.waitForVisible(locator).sendKeys(value);
    }

    protected String text(By locator) {
        return WaitUtils.waitForVisible(locator).getText();
    }
}
