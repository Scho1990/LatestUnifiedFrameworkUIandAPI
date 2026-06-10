package com.enterprise.automation.listeners;

import com.enterprise.automation.driver.DriverManager;
import com.enterprise.automation.reports.AllureAttachment;
import com.enterprise.automation.utils.ScreenshotUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        JavascriptExecutor js =
                (JavascriptExecutor)
                        DriverManager.getDriver();

        js.executeScript(
                "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"" +
                        result.getThrowable().getMessage() +
                        "\"}}");
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            AllureAttachment.attachText(throwable.toString());
        }

        try {
            AllureAttachment.attachScreenshot(ScreenshotUtil.takeScreenshot());
        } catch (WebDriverException | IllegalStateException exception) {
            AllureAttachment.attachText("Screenshot was not available: " + exception.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        JavascriptExecutor js =
                (JavascriptExecutor)
                        DriverManager.getDriver();

        js.executeScript(
                "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"passed\", \"reason\": \"Test Passed\"}}");
    }
}
