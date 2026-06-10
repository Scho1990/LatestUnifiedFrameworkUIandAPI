package com.enterprise.automation.pages;

import com.enterprise.automation.base.BasePage;
import com.enterprise.automation.config.ConfigManager;
import com.enterprise.automation.ui.models.ProductItem;
import com.enterprise.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InventoryPage extends BasePage {
    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By inventoryItems = By.cssSelector("[data-test='inventory-item']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By itemActionButton = By.cssSelector("button.btn_inventory");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");

    public String getPageTitle() {
        return text(pageTitle);
    }

    public List<ProductItem> addProductsBelowPrice(BigDecimal maxPrice, String selectedButtonText) {
        WaitUtils.waitForVisible(pageTitle);
        List<ProductItem> matchingProducts = driver().findElements(inventoryItems)
                .stream()
                .map(this::toProductItem)
                .filter(product -> product.getPrice().compareTo(maxPrice) < 0)
                .toList();

        List<ProductItem> addedProducts = new ArrayList<>();

        for (ProductItem product : matchingProducts) {
            By actionButton = productActionButton(product.getName());
            ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", WaitUtils.waitForVisible(actionButton));
            waitForButtonText(actionButton, selectedButtonText);
            addedProducts.add(product);
        }

        return addedProducts;
    }

    public String getActionButtonText(ProductItem product) {
        return text(productActionButton(product.getName()));
    }

    public int getCartCount() {
        List<WebElement> badges = driver().findElements(cartBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText());
    }

    public CartPage openCart() {
        ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", WaitUtils.waitForVisible(cartLink));
        CartPage cartPage = new CartPage();
        cartPage.waitForPageLoad();
        return cartPage;
    }

    private ProductItem toProductItem(WebElement item) {
        String name = item.findElement(itemName).getText();
        BigDecimal price = new BigDecimal(item.findElement(itemPrice).getText().replaceAll("[^0-9.]", ""));
        return new ProductItem(name, price);
    }

    private By productActionButton(String productName) {
        return By.xpath("//div[@data-test='inventory-item'][.//*[@data-test='inventory-item-name' and normalize-space()="
                + xpathLiteral(productName) + "]]//button[contains(@class,'btn_inventory')]");
    }

    private void waitForButtonText(By locator, String expectedText) {
        new WebDriverWait(driver(), Duration.ofSeconds(ConfigManager.getInt("explicit.wait.seconds")))
                .until(ExpectedConditions.textToBe(locator, expectedText));
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }
}
