package com.enterprise.automation.pages;

import com.enterprise.automation.base.BasePage;
import com.enterprise.automation.ui.models.ProductItem;
import com.enterprise.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;

public class CartPage extends BasePage {
    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By cartList = By.cssSelector("[data-test='cart-list']");
    private final By cartItems = By.cssSelector("[data-test='cart-list'] [data-test='inventory-item']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By checkoutButton = By.id("checkout");

    public void waitForPageLoad() {
        WaitUtils.waitForVisible(cartList);
    }

    public String getPageTitle() {
        return text(pageTitle);
    }

    public List<ProductItem> getCartProducts() {
        WaitUtils.waitForVisible(pageTitle);
        return driver().findElements(cartItems)
                .stream()
                .map(this::toProductItem)
                .toList();
    }

    public CheckoutInformationPage checkout() {
        ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", WaitUtils.waitForVisible(checkoutButton));
        CheckoutInformationPage checkoutInformationPage = new CheckoutInformationPage();
        checkoutInformationPage.waitForPageLoad();
        return checkoutInformationPage;
    }

    private ProductItem toProductItem(WebElement item) {
        String name = item.findElement(itemName).getText();
        BigDecimal price = new BigDecimal(item.findElement(itemPrice).getText().replaceAll("[^0-9.]", ""));
        return new ProductItem(name, price);
    }
}
