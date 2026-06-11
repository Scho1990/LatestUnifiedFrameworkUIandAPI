package com.enterprise.automation.ui.tests;

import com.enterprise.automation.base.BaseUiTest;
import com.enterprise.automation.config.ConfigManager;
import com.enterprise.automation.pages.CartPage;
import com.enterprise.automation.pages.CheckoutInformationPage;
import com.enterprise.automation.pages.CheckoutOverviewPage;
import com.enterprise.automation.pages.InventoryPage;
import com.enterprise.automation.pages.LoginPage;
import com.enterprise.automation.reports.AllureAttachment;
import com.enterprise.automation.ui.models.ProductItem;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Sauce Demo Checkout")
public class SauceDemoCheckoutTest extends BaseUiTest {
    @Test(groups = {"ui", "regression"})
    @Description("Adds all products below the configured price threshold and completes checkout")
    public void shouldCheckoutProductsBelowConfiguredPrice() {
        LoginPage loginPage = new LoginPage();

        assertThat(loginPage.isLoginButtonDisplayed())
                .as("Login button should be displayed on the login screen")
                .isTrue();

        loginPage.login(ConfigManager.getRequired("saucedemo.username"), ConfigManager.getRequired("saucedemo.password"));

        InventoryPage inventoryPage = new InventoryPage();
        assertThat(inventoryPage.getPageTitle())
                .as("Inventory page title")
                .isEqualTo(ConfigManager.getRequired("saucedemo.inventory.title"));

        BigDecimal maxProductPrice = new BigDecimal(ConfigManager.getRequired("saucedemo.max.product.price"));
        String selectedButtonText = ConfigManager.getRequired("saucedemo.remove.button.text");
        List<ProductItem> addedProducts = inventoryPage.addProductsBelowPrice(maxProductPrice, selectedButtonText);

        if (addedProducts.isEmpty()) {
            AllureAttachment.attachText("No product found less than " + maxProductPrice.stripTrailingZeros().toPlainString() + "$");
            return;
        }

        assertThat(addedProducts)
                .as("Added product buttons should change to Remove")
                .allSatisfy(product -> assertThat(inventoryPage.getActionButtonText(product))
                        .isEqualTo(selectedButtonText));
        assertThat(inventoryPage.getCartCount())
                .as("Cart badge count")
                .isEqualTo(addedProducts.size());

        CartPage cartPage = inventoryPage.openCart();
        assertThat(cartPage.getPageTitle())
                .as("Cart page title")
                .isEqualTo(ConfigManager.getRequired("saucedemo.cart.title"));

        assertThat(cartPage.getCartProducts())
                .as("Cart products")
                .containsExactlyInAnyOrderElementsOf(addedProducts);

        CheckoutInformationPage checkoutInformationPage = cartPage.checkout();
        assertThat(checkoutInformationPage.getPageTitle())
                .as("Checkout information page title")
                .isEqualTo(ConfigManager.getRequired("saucedemo.checkout.information.title"));
        CheckoutOverviewPage checkoutOverviewPage = checkoutInformationPage.enterCustomerInformation(
                ConfigManager.getRequired("checkout.first.name"),
                ConfigManager.getRequired("checkout.last.name"),
                ConfigManager.getRequired("checkout.postal.code")
        );

        assertThat(checkoutOverviewPage.getPageTitle())
                .as("Checkout overview page title")
                .isEqualTo(ConfigManager.getRequired("saucedemo.checkout.overview.title"));

        assertThat(checkoutOverviewPage.getTotal())
                .as("Checkout total should equal subtotal plus tax")
                .isEqualByComparingTo(checkoutOverviewPage.calculateExpectedTotal());

        checkoutOverviewPage.finishCheckout();
    }
}
