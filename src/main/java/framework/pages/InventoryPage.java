package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {
    private final By title = By.cssSelector(".title");
    private final By backpackAddButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By bikeLightAddButton = By.id("add-to-cart-sauce-labs-bike-light");
    private final By cartLink = By.cssSelector(".shopping_cart_link");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");

    public InventoryPage(WebDriver driver, int timeoutSeconds) {
        super(driver, timeoutSeconds);
    }

    public String getTitle() {
        return text(title);
    }

    public void addBackpackToCart() {
        click(backpackAddButton);
    }

    public void addBikeLightToCart() {
        click(bikeLightAddButton);
    }

    public void openCart() {
        click(cartLink);
    }

    public String getCartBadgeValue() {
        return text(cartBadge);
    }
}
