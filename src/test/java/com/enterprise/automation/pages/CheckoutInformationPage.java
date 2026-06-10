package com.enterprise.automation.pages;

import com.enterprise.automation.base.BasePage;
import com.enterprise.automation.reports.AllureAttachment;
import com.enterprise.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CheckoutInformationPage extends BasePage {
    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public void waitForPageLoad() {
        WaitUtils.waitForVisible(firstNameInput);
    }

    public String getPageTitle() {
        return text(pageTitle);
    }

    public CheckoutOverviewPage enterCustomerInformation(String firstName, String lastName, String postalCode) {
        setInputValue(firstNameInput, firstName);
        setInputValue(lastNameInput, lastName);
        setInputValue(postalCodeInput, postalCode);
        ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", WaitUtils.waitForVisible(continueButton));
        CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage();
        try {
            checkoutOverviewPage.waitForPageLoad();
        } catch (TimeoutException exception) {
            String diagnostics = checkoutDiagnostics();
            AllureAttachment.attachText(diagnostics);
            throw new AssertionError(diagnostics, exception);
        }
        return checkoutOverviewPage;
    }

    private void setInputValue(By locator, String value) {
        WebElement element = WaitUtils.waitForVisible(locator);
        ((JavascriptExecutor) driver()).executeScript(
                "Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set.call(arguments[0], arguments[1]);"
                        + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element,
                value
        );
    }

    private String checkoutDiagnostics() {
        return "Checkout information did not navigate to overview. "
                + "Url: " + driver().getCurrentUrl()
                + ", title: " + getPageTitle()
                + ", firstName: " + WaitUtils.waitForVisible(firstNameInput).getAttribute("value")
                + ", lastName: " + WaitUtils.waitForVisible(lastNameInput).getAttribute("value")
                + ", postalCode: " + WaitUtils.waitForVisible(postalCodeInput).getAttribute("value")
                + ", error: " + getErrorMessage();
    }

    private String getErrorMessage() {
        List<WebElement> errors = driver().findElements(errorMessage);
        if (errors.isEmpty()) {
            return "";
        }
        return errors.get(0).getText();
    }
}
