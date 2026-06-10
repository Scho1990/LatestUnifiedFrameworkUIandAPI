package com.enterprise.automation.ui.tests;

import com.enterprise.automation.base.BaseUiTest;
import com.enterprise.automation.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Login")
public class LoginTest extends BaseUiTest {
    @Test(groups = {"ui", "smoke"})
    @Description("Validates login error handling through the Login page object")
    public void shouldShowErrorForInvalidCredentials() {
        LoginPage loginPage = new LoginPage().login("locked_out_user", "wrong_password");

        assertThat(loginPage.getErrorMessage())
                .as("Login error message")
                .containsIgnoringCase("Epic sadface");
    }
}
