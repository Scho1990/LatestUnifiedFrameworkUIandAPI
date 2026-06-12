package com.enterprise.automation.pages;

import com.enterprise.automation.base.BasePage;
import com.enterprise.automation.utils.WaitUtils;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error1']");

    public boolean isLoginButtonDisplayed() {
        return WaitUtils.waitForVisible(loginButton).isDisplayed();
    }

    public LoginPage login(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return this;
    }

    public String getErrorMessage() {
        return text(errorMessage);
    }
}
