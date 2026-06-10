package com.enterprise.automation.base;

import com.enterprise.automation.config.ConfigManager;
import com.enterprise.automation.driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public abstract class BaseUiTest {
    @BeforeMethod(alwaysRun = true)
    public void setUpBrowser(Method method) {
        String testMethodName = method.getName();
        DriverManager.initializeDriver(testMethodName);
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().get(ConfigManager.getRequired("ui.base.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {
        DriverManager.quitDriver();
    }
}
