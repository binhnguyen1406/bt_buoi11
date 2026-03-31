package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {
    private final By cartItem = By.cssSelector(".cart_item");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver, int timeoutSeconds) {
        super(driver, timeoutSeconds);
    }

    public boolean hasItem() {
        return !driver.findElements(cartItem).isEmpty();
    }

    public void clickCheckout() {
        click(checkoutButton);
    }
}
