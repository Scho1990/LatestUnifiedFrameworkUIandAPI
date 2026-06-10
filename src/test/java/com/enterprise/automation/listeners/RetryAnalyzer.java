package com.enterprise.automation.listeners;

import com.enterprise.automation.config.ConfigManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int attempt = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetryCount = ConfigManager.getInt("retry.count");
        if (attempt < maxRetryCount) {
            attempt++;
            return true;
        }
        return false;
    }
}
