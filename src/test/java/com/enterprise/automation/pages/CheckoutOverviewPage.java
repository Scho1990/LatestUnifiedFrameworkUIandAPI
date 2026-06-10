package com.enterprise.automation.pages;

import com.enterprise.automation.base.BasePage;
import com.enterprise.automation.utils.WaitUtils;
import org.openqa.selenium.By;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CheckoutOverviewPage extends BasePage {
    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By subtotalLabel = By.cssSelector("[data-test='subtotal-label']");
    private final By taxLabel = By.cssSelector("[data-test='tax-label']");
    private final By totalLabel = By.cssSelector("[data-test='total-label']");
    private final By finishButton = By.id("finish");

    public void waitForPageLoad() {
        WaitUtils.waitForVisible(totalLabel);
    }

    public String getPageTitle() {
        return text(pageTitle);
    }

    public BigDecimal getSubtotal() {
        return extractAmount(text(subtotalLabel));
    }

    public BigDecimal getTax() {
        return extractAmount(text(taxLabel));
    }

    public BigDecimal getTotal() {
        return extractAmount(text(totalLabel));
    }

    public BigDecimal calculateExpectedTotal() {
        return getSubtotal().add(getTax()).setScale(2, RoundingMode.HALF_UP);
    }

    public void finishCheckout() {
        click(finishButton);
    }

    private BigDecimal extractAmount(String amountText) {
        return new BigDecimal(amountText.replaceAll("[^0-9.]", "")).setScale(2, RoundingMode.HALF_UP);
    }
}
